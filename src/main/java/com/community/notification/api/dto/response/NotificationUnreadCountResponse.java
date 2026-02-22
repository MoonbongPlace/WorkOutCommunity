package com.community.notification.api.dto.response;

import com.community.notification.application.dto.UnReadCountResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class NotificationUnreadCountResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private UnReadCountResult unReadCountResult;

    public static NotificationUnreadCountResponse from(UnReadCountResult unReadCountResult, String message) {
            return new NotificationUnreadCountResponse(
                    String.valueOf(HttpStatus.OK.value()),
                    message,
                    OffsetDateTime.now(),
                    unReadCountResult
            );
    }
}
