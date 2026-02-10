package com.community.global;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain
    ) throws ServletException, IOException {

        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = resolveBearerToken(request);

        // permitAll 엔드포인트는 토큰 없이도 통과
        if (token==null){
            filterChain.doFilter(request, response);
            return;
        }

        try{
            Long memberId = jwtProvider.extractMemberId(token);
            String role = jwtProvider.extractRole(token);

            String authority = "ROLE_" + role.toUpperCase(); // ROLE_USER / ROLE_ADMIN
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(authority));

            CustomUserPrincipal principal = new CustomUserPrincipal(memberId, authorities);

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            // SecurityContextHolder 에 세팅. 현재 요청 스레드에서의 로그인 사용자 결정.
            // Controller 에서 Authentication, @AuthenticationPrincipal 로 꺼낼 수 있음.
            SecurityContextHolder.getContext().setAuthentication(auth);
        } catch(CommonException e) {
            SecurityContextHolder.clearContext();
            writeUnauthorized(response, e.code());
            return;
        }
        filterChain.doFilter(request, response);
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
        if(header == null) return null;

        if (header.startsWith("Bearer ")){
            return header.substring(7);
        }
        return null;
    }
}
