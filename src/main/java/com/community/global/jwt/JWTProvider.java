package com.community.global.jwt;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class JWTProvider {

    private final JWTProperties jwtProperties;
    private final SecretKey key;

    public JWTProvider(JWTProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public TokenPairDTO issueTokenPair(Long memberId, String role){
        String at = issueAccessToken(memberId, role);
        String rt = issueRefreshToken(memberId);

        return new TokenPairDTO(at, rt, jwtProperties.accessExpSeconds(), jwtProperties.refreshExpSeconds());
    }

    public String issueAccessToken(Long memberId, String role) {
        Map<String, Object> claims = Map.of("role", role);
        return issueToken(String.valueOf(memberId), jwtProperties.accessExpSeconds(), claims);
    }

    public String issueRefreshToken(Long memberId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())           // 동시 발급 시 해시 충돌 방지
                .subject(String.valueOf(memberId))
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(jwtProperties.refreshExpSeconds())))
                .signWith(key)
                .compact();
    }

    /**
     * AccessToken 전용 파싱.
     * 서명 검증 + 만료 검증 + AT 필수 클레임(role) 존재 여부까지 한 번에 수행.
     * 필터에서 이 메서드 하나만 호출해 Claims를 재사용하면 중복 파싱이 없다.
     */
    public Claims parseAccessToken(String token) {
        Claims claims = verifyAndGetClaims(token);
        if (claims.get("role") == null) {
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
        return claims;
    }

    /**
     * 서명/만료 검증 후 Claims 반환. RefreshToken 처리(AuthService) 등 서비스 레이어에서 재사용.
     */
    public Claims verifyAndGetClaims(String token) {
        if (token == null || token.isBlank()) {
            throw new CommonException(ResponseCode.TOKEN_MISSING);
        }
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new CommonException(ResponseCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
    }

    public Long extractMemberId(String token) {
        String sub = verifyAndGetClaims(token).getSubject();
        try {
            return Long.parseLong(sub);
        } catch (NumberFormatException e) {
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
    }

    /** 이미 파싱된 Claims에서 role 추출 — 이중 파싱 없이 재사용 가능 */
    public String extractRole(Claims claims) {
        String role = (String) claims.get("role");
        if (role == null) throw new CommonException(ResponseCode.TOKEN_INVALID);
        return role;
    }

    public long getRtExpSeconds() { return jwtProperties.refreshExpSeconds(); }
    public long getAtExpSeconds()  { return jwtProperties.accessExpSeconds(); }
    public boolean getCookieSecure()   { return jwtProperties.cookieSecure(); }
    public String  getCookieSameSite() { return jwtProperties.cookieSameSite(); }

    private String issueToken(String subject, long expSeconds, Map<String, Object> claims) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(expSeconds)))
                .claims(claims)
                .signWith(key)
                .compact();
    }
}
