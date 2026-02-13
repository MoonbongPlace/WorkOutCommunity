package com.community.global;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

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

        return new TokenPairDTO(at, rt, jwtProperties.accessExpSeconds(), jwtProperties.refreshExpSeconds() );
    }


    public String issueAccessToken(Long memberId, String role) {
        Map<String, Object> claims = Map.of("role", role);
        return issueToken(String.valueOf(memberId), jwtProperties.accessExpSeconds(), claims);
    }

    public String issueRefreshToken(Long memberId) {
        return issueToken(String.valueOf(memberId), jwtProperties.refreshExpSeconds(), Map.of());
    }

    private String issueToken(String subject, long expSeconds, Map<String, Object> claims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(expSeconds);

        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .claims(claims)
                .signWith(key)
                .compact();
    }

    public Claims verifyAndGetClaims(String token){

        if (token==null || token.isBlank()) {
            throw new CommonException(ResponseCode.TOKEN_MISSING);
        }

        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            // exp 만료 시 예외 발생
        } catch (ExpiredJwtException e) {
            throw new CommonException(ResponseCode.TOKEN_EXPIRED);
        } catch(SecurityException | UnsupportedJwtException | IllegalArgumentException e){
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
    }

    public Long extractMemberId(String token){
        String sub = verifyAndGetClaims(token).getSubject();
        try {
            return Long.parseLong(sub);
        }catch (Exception e){
            throw new CommonException(ResponseCode.TOKEN_INVALID);
        }
    }

    public String extractRole(String token){
        Claims claims = verifyAndGetClaims(token);
        String role = (String) claims.get("role");

        if (role == null) throw new CommonException(ResponseCode.TOKEN_INVALID);

        return role;
    }


    public long getRtExpSeconds() {
        return jwtProperties.refreshExpSeconds();
    }

    public long getAtExpSeconds() { return jwtProperties.accessExpSeconds(); }

    public boolean getCookieSecure() { return jwtProperties.cookieSecure(); }

    public String getCookieSameSite() { return jwtProperties.cookieSameSite(); }
}
