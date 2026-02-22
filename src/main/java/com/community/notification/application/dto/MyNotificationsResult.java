package com.community.notification.application.dto;

import com.community.notification.domain.model.Notification;
import com.community.notification.domain.model.NotificationType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;
import java.util.List;

@JsonPropertyOrder({"notifications", "pageInfo", "unreadCount"})
public record MyNotificationsResult(List<NotificationItem> notifications,
                                    MyNotificationsResult.PageInfo pageInfo,
                                    long unreadCount) {
    @Getter
    @AllArgsConstructor
    public static class NotificationItem {
        private final Long id;
        private final Long postId;
        private final NotificationType type;
        private final String message;
        private final String linkUrl;
        private final boolean isRead;
        private OffsetDateTime createdAt;
        private OffsetDateTime readAt;

        public static NotificationItem from(Notification notification) {
            return new NotificationItem(
                    notification.getId(),
                    notification.getPostId(),
                    notification.getType(),
                    notification.getMessage(),
                    notification.getLinkUrl(),
                    notification.isRead(),
                    notification.getCreatedAt(),
                    notification.getReadAt()
            );
        }

    }

    public record PageInfo(int page, int size, long totalElements, int totalPages, boolean first, boolean last) {
        public static PageInfo from(Page<?> page) {
            return new PageInfo(
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isFirst(),
                    page.isLast()
            );
        }
    }

}
