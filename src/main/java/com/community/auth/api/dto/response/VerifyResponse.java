package com.community.auth.api.dto.response;

import com.community.auth.application.dto.PhoneVerifyResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class VerifyResponse {
    private final String code;
    private final String message;
    private final OffsetDateTime timestamp;
    private final PhoneVerifyResult verifyResult;

    public static VerifyResponse from(PhoneVerifyResult verifyResult, String message) {
        return new VerifyResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                verifyResult
        );
    }
}
