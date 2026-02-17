package com.community.notification.api.dto.response;

import com.community.notification.application.NotificationCreateResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class NotificationCreateResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private NotificationCreateResult notificationCreateResult;

    public static NotificationCreateResponse from(NotificationCreateResult notificationCreateResult, String message) {
        return new NotificationCreateResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                notificationCreateResult
        );
    }
}
