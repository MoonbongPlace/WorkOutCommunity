package com.community.auth.api.dto.response;

import com.community.auth.application.EmailVerifyResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class EmailVerifyResponse {
    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;
    private final EmailVerifyResult emailVerifyResult;

    public static EmailVerifyResponse from(EmailVerifyResult emailVerifyResult, String message) {
        return new EmailVerifyResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                emailVerifyResult
        );
    }
}
