package com.community.notification.api.dto.response;

import com.community.notification.application.ReadAllResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class NotificationReadAllResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private int count;

    public static NotificationReadAllResponse of(String message, int count) {
        return new NotificationReadAllResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                count
        );
    }
}
