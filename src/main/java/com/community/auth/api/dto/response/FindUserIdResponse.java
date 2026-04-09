package com.community.auth.api.dto.response;

import com.community.auth.application.dto.FindUserIdResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class FindUserIdResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private FindUserIdResult findUserIdResult;

    public static FindUserIdResponse from(FindUserIdResult findUserIdResult, String message) {
        return new FindUserIdResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                findUserIdResult
        );
    }
}
