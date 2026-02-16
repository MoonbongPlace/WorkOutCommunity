package com.community.notification.application;

import com.community.notification.api.dto.request.NotificationCreateRequest;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"notificationId", "memberId", "senderId", "postId", "createdAt", "message"})
public class NotificationCreateResult {
    Long notificationId;
    Long memberId;
    Long senderId;
    Long postId;
    OffsetDateTime createdAt;
    String message;

    public static NotificationCreateResult of(@Valid NotificationCreateRequest request) {
        return new NotificationCreateResult(
                request.getId
        );
    }
}
