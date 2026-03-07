package com.community.notification.application.dto;

import com.community.notification.domain.model.Notification;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"notificationId", "memberId", "senderId", "postId", "createdAt", "message"})
public class CreateNotificationPostLikeResult {
    private boolean created;
    private Long notificationId;
    private Long memberId;
    private Long senderId;
    private Long postId;
    private OffsetDateTime createdAt;
    private String message;

    public static CreateNotificationPostLikeResult of(Notification saved) {
        return new CreateNotificationPostLikeResult(
                true,
                saved.getId(),
                saved.getMemberId(),
                saved.getSenderId(),
                saved.getPostId(),
                saved.getCreatedAt(),
                saved.getMessage()
        );
    }

    public static CreateNotificationPostLikeResult skipped() {
        return new CreateNotificationPostLikeResult(
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
