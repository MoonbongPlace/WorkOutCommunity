package com.community.auth.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class LogoutResponse {
    String code;
    String message;
    OffsetDateTime timestamp;

    public static LogoutResponse from(String message) {
        return new LogoutResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now()
        );
    }
}
