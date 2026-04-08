package com.community.auth.api.dto.response;

import com.community.auth.application.dto.MemberSignupResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class    SignupResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private MemberSignupResult memberSignupResult;

    public static SignupResponse from(MemberSignupResult memberSignupResult, String message) {
        return new SignupResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                memberSignupResult
        );
    }
}
