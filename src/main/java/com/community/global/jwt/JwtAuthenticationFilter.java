package com.community.global.jwt;

import com.community.global.CustomUserPrincipal;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /**
     * 필터를 완전히 건너뛸 경로 목록.
     * - OPTIONS  : CORS preflight 는 인증 불필요
     * - auth/**  : 로그인·회원가입·토큰재발행 등 공개 엔드포인트.
     *              만료된 AT가 헤더에 붙어 있어도 401 없이 통과해야 함.
     * - swagger  : 개발 편의용 공개 문서 경로
     */
    private static final List<String> SKIP_PATHS = List.of(
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // (4) CORS preflight 는 무조건 스킵
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        // (2) permitAll 공개 경로 스킵 — 토큰 유무·만료 여부와 무관하게 통과
        String uri = request.getRequestURI();
        return SKIP_PATHS.stream().anyMatch(pattern -> PATH_MATCHER.match(pattern, uri));
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        String token = resolveBearerToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // (1)(3) AT 검증 + Claims 파싱을 1회만 수행.
            //        parseAccessToken 이 서명·만료·role 클레임 존재를 한 번에 검증한다.
            Claims claims = jwtProvider.parseAccessToken(token);

            Long memberId = parseMemberId(claims);
            String role   = jwtProvider.extractRole(claims);

            // (5) 활성 회원 검증 — 즉시 반영이 필요한 정책이므로 유지.
            //     트래픽 증가 시 Redis 캐시(TTL ≤ AT TTL) 로 교체 가능.
            memberRepositoryAdapter.findActiveById(memberId)
                    .orElseThrow(() -> new CommonException(ResponseCode.UNAUTHORIZED));

            List<GrantedAuthority> authorities =
                    List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            new CustomUserPrincipal(memberId, authorities), null, authorities));

        } catch (CommonException e) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(response, e.code());
            return;
        }

        filterChain.doFilter(request, response);
    }

    /** claims.getSubject() → Long 변환 실패 시 TOKEN_INVALID */
    private Long parseMemberId(Claims claims) {
        try {
            return Long.parseLong(claims.getSubject());
        } catch (NumberFormatException e) {
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
    }

    private void writeUnauthorized(HttpServletResponse response, ResponseCode code) throws IOException {
        response.setStatus(code.getStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
                {"code":"%s","message":"%s"}
                """.formatted(code.getCode(), code.getMessage()));
    }

    private String resolveBearerToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return null;
        return header.substring(7);
    }
}
