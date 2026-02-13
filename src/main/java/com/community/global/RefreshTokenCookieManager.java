package com.community.global;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenCookieManager {
    private final JWTProvider jwtProvider;

    private static final String COOKIE_NAME = "refreshToken";

    public void addRefreshTokenCookie(HttpServletResponse response,String token){
        long maxAge = jwtProvider.getRtExpSeconds();

        String cookie = COOKIE_NAME + "=" + token
                + "; Max-Age=" + maxAge
                + "; Path=/api/v1/auth"
                + "; HttpOnly"
                + (jwtProvider.getCookieSecure() ? "; Secure" : "")
                + "; SameSite=" + jwtProvider.getCookieSameSite();

        response.addHeader("Set-Cookie", cookie);
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response){
        String cookie = COOKIE_NAME + "="
                + "; Max-Age=0"
                + "; Path=/api/v1/auth"
                + "; HttpOnly"
                + (jwtProvider.getCookieSecure() ? "; Secure" : "")
                + "; SameSite=" + jwtProvider.getCookieSameSite();

        response.addHeader("Set-Cookie", cookie);
    }

    public Optional<String> getRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(c -> COOKIE_NAME.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}
