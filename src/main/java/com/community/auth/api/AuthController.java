package com.community.auth.api;

import com.community.auth.api.dto.request.*;
import com.community.auth.api.dto.response.*;
import com.community.auth.application.AuthService;
import com.community.auth.application.dto.EmailVerifyResult;
import com.community.auth.application.dto.*;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.global.jwt.RefreshTokenCookieManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발행 / 로그아웃")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    // 회원가입
    @PostMapping(value = "/signup")
    public ResponseEntity<SignupResponse> signup(
            @RequestBody @Valid SignupRequest request
    ) {
        MemberSignupResult memberSignupResult = authService.signup(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SignupResponse.from(memberSignupResult, "회원가입 성공"));
    }

    // 이메일 인증 : 인증번호 발급
    // 인증 번호 ID, PHONE NUMBER 응답 (임시)
    @PostMapping("/email-verifications")
    public ResponseEntity<EmailVerifyResponse> personalCode(
            @RequestBody @Valid final EmailRequest request
    ){
        EmailVerifyResult emailVerifyResult = authService.emailPersonalCode(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(EmailVerifyResponse.from(emailVerifyResult, "인증번호 발급"));
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
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        String refreshToken = refreshTokenCookieManager.getRefreshTokenFromCookie(request)
                .orElseThrow(() -> new CommonException(ResponseCode.INVALID_REFRESH_TOKEN));

        ReissueResponse reissueResponse = authService.reissue(refreshToken);

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
