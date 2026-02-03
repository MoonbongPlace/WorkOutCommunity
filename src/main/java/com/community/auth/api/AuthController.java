package com.community.auth.api;

import com.community.auth.api.dto.request.SignupRequest;
import com.community.auth.api.dto.response.SignupResponse;
import com.community.auth.application.AuthService;
import com.community.auth.application.MemberSignupResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
