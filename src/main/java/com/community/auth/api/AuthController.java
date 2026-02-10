package com.community.auth.api;

import com.community.auth.api.dto.request.ReissueRequest;
import com.community.auth.api.dto.response.ReissueResponse;
import com.community.auth.api.dto.response.SigninResponse;
import com.community.auth.api.dto.request.SigninRequest;
import com.community.auth.api.dto.request.SignupRequest;
import com.community.auth.api.dto.response.SignupResponse;
import com.community.auth.application.AuthService;
import com.community.auth.application.dto.MemberSigninResult;
import com.community.auth.application.dto.MemberSignupResult;
import com.community.global.*;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

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
            @RequestBody @Valid final SigninRequest request
    ){
        MemberSigninResult memberSigninResult = authService.signin(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(SigninResponse.from(memberSigninResult, "로그인 성공"));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ReissueResponse> reissue(
            @RequestBody @Valid ReissueRequest request) {

        ReissueResponse reissueResponse = authService.reissue(request);

        return ResponseEntity.ok(reissueResponse);
    }

    // 로그아웃
    @PostMapping("/logout")
    public void logout(@RequestBody @Valid ReissueRequest request) {
        authService.logout(request);
        
    }

}
