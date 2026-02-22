package com.community.notification.domain.model;

import jakarta.persistence.*;
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

    @Column(name = "post_id")
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

    // 댓글 알림
    public static Notification createComment(
            Long receiverId,
            Long senderId,
            Long postId,
            String message,
            String linkUrl
    ) {
        return create(
                receiverId,
                senderId,
                postId,
                NotificationType.COMMENT,
                message,
                linkUrl
        );
    }

    // 브로드캐스트 알림
    public static Notification createBroadcast(
            Long receiverId,
            Long adminId,
            String message,
            String linkUrl
    ) {
        return create(
                receiverId,
                adminId,
                null,
                NotificationType.BROADCAST,
                message,
                linkUrl
        );
    }

    public static Notification create(
            Long memberId,
            Long senderId,
            Long postId,
            NotificationType type,
            String message,
            String linkUrl
    ) {
        Notification notification = new Notification();
        notification.memberId = memberId;
        notification.senderId = senderId;
        notification.postId = postId;
        notification.type = type;
        notification.message = message;
        notification.linkUrl = linkUrl;
        notification.isRead = false;
        notification.readAt = null;
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
