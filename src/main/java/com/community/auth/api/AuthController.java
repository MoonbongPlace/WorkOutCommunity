package com.community.auth.api;

import com.community.auth.api.dto.response.LogoutResponse;
import com.community.auth.api.dto.response.ReissueResponse;
import com.community.auth.api.dto.response.SigninResponse;
import com.community.auth.api.dto.request.SigninRequest;
import com.community.auth.api.dto.request.SignupRequest;
import com.community.auth.api.dto.response.SignupResponse;
import com.community.auth.application.AuthService;
import com.community.auth.application.dto.MemberSigninResult;
import com.community.auth.application.dto.MemberSignupResult;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.global.jwt.RefreshTokenCookieManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import jakarta.validation.ConstraintViolation;
import java.util.Set;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Auth", description = "회원가입 / 로그인 / 토큰 재발행 / 로그아웃")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final ObjectMapper objectMapper;
    private final Validator validator;
    private final RefreshTokenCookieManager refreshTokenCookieManager;

    // 회원가입
    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SignupResponse> signup(
            @RequestPart("data") String data,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage
    ) throws JsonProcessingException {

        SignupRequest request = objectMapper.readValue(data, SignupRequest.class);

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            throw new CommonException(ResponseCode.INVALID_REQUEST);
        }

        MemberSignupResult memberSignupResult = authService.signup(request,  profileImage);

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
