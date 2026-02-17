package com.community.notification.application;

import com.community.notification.api.dto.request.NotificationCreateRequest;
import com.community.notification.domain.model.Notification;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"notificationId", "memberId", "senderId", "postId", "createdAt", "message"})
public class NotificationCreateResult {
    private boolean created;
    private Long notificationId;
    private Long memberId;
    private Long senderId;
    private Long postId;
    private OffsetDateTime createdAt;
    private String message;

    public static NotificationCreateResult of(Notification saved) {
        return new NotificationCreateResult(
                true,
                saved.getId(),
                saved.getMemberId(),
                saved.getSenderId(),
                saved.getPostId(),
                saved.getCreatedAt(),
                saved.getMessage()
        );
    }

    public static NotificationCreateResult skipped() {
        return new NotificationCreateResult(
                false,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }
}
