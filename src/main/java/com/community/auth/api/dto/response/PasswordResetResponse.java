package com.community.auth.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@AllArgsConstructor
@Getter
public class PasswordResetResponse {
    private String message;
    private OffsetDateTime timestamp;

    public static PasswordResetResponse of(String message) {
        return new PasswordResetResponse(
                message,
                OffsetDateTime.now()
        );
    }
}
