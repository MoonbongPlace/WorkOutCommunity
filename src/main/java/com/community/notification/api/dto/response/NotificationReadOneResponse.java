package com.community.notification.api.dto.response;

import com.community.notification.application.dto.ReadOneResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class NotificationReadOneResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private ReadOneResult readOneResult;

    public static NotificationReadOneResponse from(ReadOneResult readOneResult, String message) {
        return new NotificationReadOneResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                readOneResult
        );
    }
}
