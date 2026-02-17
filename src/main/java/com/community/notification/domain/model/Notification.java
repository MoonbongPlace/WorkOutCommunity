package com.community.notification.domain.model;

import com.community.notification.api.dto.request.NotificationCreateRequest;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String message;

    @Column(name = "link_url")
    private String linkUrl;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "sender_id", nullable = false)
    private Long senderId;

    public static Notification create(
            Long receiverId,
            Long senderId,
            NotificationCreateRequest request,
            String linkUrl
    ) {
        Notification notification = new Notification();
        notification.setMemberId(receiverId);
        notification.setPostId(request.getPostId());
        notification.setType(request.getType());
        notification.setMessage(request.getMessage());
        notification.setLinkUrl(linkUrl);
        notification.setRead(false);
        notification.setCreatedAt(OffsetDateTime.now());
        notification.setSenderId(senderId);
        return notification;
    }

    public static Notification of(
            Long receiverId,
            Long senderId,
            Long postId,
            NotificationType type,
            String message,
            String linkUrl
    ) {
        Notification notification = new Notification();
        notification.memberId = receiverId;
        notification.senderId = senderId;
        notification.postId = postId;
        notification.type = type;
        notification.message = message;
        notification.linkUrl = linkUrl;
        notification.isRead = false;
        notification.createdAt = OffsetDateTime.now();
        return notification;
    }


    public void markRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = OffsetDateTime.now();
        }
    }

}
