package com.community.notification.api.dto.response;

import com.community.notification.application.dto.MyNotificationsResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class NotificationResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private MyNotificationsResult myNotificationsResult;

    public static NotificationResponse from(MyNotificationsResult myNotificationsResult, String message) {
        return new NotificationResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                myNotificationsResult
        );
    }
}
