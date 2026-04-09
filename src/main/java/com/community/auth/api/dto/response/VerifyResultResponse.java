package com.community.auth.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class VerifyResultResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public static VerifyResultResponse from(String message) {
            return new VerifyResultResponse(
                    String.valueOf(HttpStatus.OK.value()),
                    message,
                    OffsetDateTime.now()
            );
    }
}
