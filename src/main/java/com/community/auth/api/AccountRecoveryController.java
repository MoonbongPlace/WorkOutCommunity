package com.community.auth.api;

import com.community.auth.api.dto.request.FindUserIdRequest;
import com.community.auth.api.dto.request.PasswordResetRequest;
import com.community.auth.api.dto.request.VerifyRequest;
import com.community.auth.api.dto.request.VerifyResultRequest;
import com.community.auth.api.dto.response.FindUserIdResponse;
import com.community.auth.api.dto.response.PasswordResetResponse;
import com.community.auth.api.dto.response.VerifyResponse;
import com.community.auth.api.dto.response.VerifyResultResponse;
import com.community.auth.application.AccountRecoveryService;
import com.community.auth.application.dto.FindUserIdResult;
import com.community.auth.application.dto.PhoneVerifyResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "AccountRecovery", description = "아이디 찾기 / 인증번호 발급 / 인증번호 검증")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/account-recovery")
public class AccountRecoveryController {

    private final AccountRecoveryService accountRecoveryService;

    // 아이디 찾기
    @PostMapping("/user-id")
    public ResponseEntity<FindUserIdResponse> findUserId(
            @RequestBody @Valid final FindUserIdRequest request
    ){
        FindUserIdResult findUserIdResult = accountRecoveryService.findMember(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(FindUserIdResponse.from(findUserIdResult, "아이디 찾기 성공"));
    }

    // 번호 인증 : 인증번호 발급
    @PostMapping("/phone-verifications")
    public ResponseEntity<VerifyResponse> sendVerificationCode(
            @RequestBody @Valid final VerifyRequest request
    ){
        PhoneVerifyResult verifyResult = accountRecoveryService.sendVerificationCode(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(VerifyResponse.from(verifyResult, "인증번호 발급"));
    }

    // 번호 인증 : 인증번호 검증
    @PostMapping("/phone-verifications-result")
    public ResponseEntity<VerifyResultResponse> verifyVerificationCode(
            @RequestBody @Valid final VerifyResultRequest request
    ){
        VerifyResultResponse verifyResultResponse = accountRecoveryService.verifyVerificationCode(request);

        return ResponseEntity.ok(verifyResultResponse);
    }

    // 비밀번호 초기화
    @PostMapping("/password-reset")
    ResponseEntity<PasswordResetResponse> passwordReset(
            @RequestBody @Valid PasswordResetRequest request
    ){
        PasswordResetResponse passwordResetResponse = accountRecoveryService.requestPasswordReset(request);
        return ResponseEntity.ok(passwordResetResponse);
    }
}
