package com.community.auth.api;

import com.community.auth.api.dto.request.ReissueRequest;
import com.community.auth.api.dto.response.LogoutResponse;
import com.community.auth.api.dto.response.ReissueResponse;
import com.community.auth.api.dto.response.SigninResponse;
import com.community.auth.api.dto.request.SigninRequest;
import com.community.auth.api.dto.request.SignupRequest;
import com.community.auth.api.dto.response.SignupResponse;
import com.community.auth.application.AuthService;
import com.community.auth.application.dto.MemberSigninResult;
import com.community.auth.application.dto.MemberSignupResult;
import com.community.global.jwt.RefreshTokenCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody @Valid final SignupRequest request
    ){
        MemberSignupResult memberSignupResult = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SignupResponse.from(memberSignupResult, "회원가입 성공"));
    }

    // 로그인
    @PostMapping("/signin")
    public ResponseEntity<SigninResponse> signin(
            @RequestBody @Valid final SigninRequest request,
            HttpServletResponse response
    ){
        MemberSigninResult memberSigninResult = authService.signin(request);

        refreshTokenCookieManager.addRefreshTokenCookie(response, memberSigninResult.getRefreshToken());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SigninResponse.from(memberSigninResult, "로그인 성공"));
    }

    // 토큰 재발행
    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(
            @RequestBody @Valid ReissueRequest request,
            HttpServletResponse response
    ) {
        ReissueResponse reissueResponse = authService.reissue(request);

        refreshTokenCookieManager.addRefreshTokenCookie(response, reissueResponse.getRefreshToken());

        return ResponseEntity.ok(reissueResponse);
    }

    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(HttpServletResponse response, HttpServletRequest request) {

        refreshTokenCookieManager.getRefreshTokenFromCookie(request).ifPresent(authService::logout);

        refreshTokenCookieManager.deleteRefreshTokenCookie(response);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(LogoutResponse.from("로그아웃 성공"));
    }
}
