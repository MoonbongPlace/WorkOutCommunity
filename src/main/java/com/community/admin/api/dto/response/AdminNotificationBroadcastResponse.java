package com.community.admin.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminNotificationBroadcastResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private long sent;

    public static AdminNotificationBroadcastResponse of(long sent, String message) {
        return new AdminNotificationBroadcastResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                sent
        );
    }
}
