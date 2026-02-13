package com.community.auth.api.dto.response;

import com.community.auth.application.dto.MemberSigninResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class SigninResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private MemberSigninResult memberSigninResult;

    public static SigninResponse from(MemberSigninResult memberSigninResult, String message) {
        return new SigninResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                memberSigninResult
        );
    }
}
