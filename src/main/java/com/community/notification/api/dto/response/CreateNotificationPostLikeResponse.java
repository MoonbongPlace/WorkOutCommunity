package com.community.notification.api.dto.response;

import com.community.notification.application.dto.CreateNotificationPostLikeResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CreateNotificationPostLikeResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CreateNotificationPostLikeResult notificationPostLikeResult;

    public static CreateNotificationPostLikeResponse from(CreateNotificationPostLikeResult notificationPostLikeResult, String message) {
        return new CreateNotificationPostLikeResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                notificationPostLikeResult
        );
    }
}
