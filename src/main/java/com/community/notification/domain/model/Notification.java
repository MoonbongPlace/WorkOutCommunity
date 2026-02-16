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
    private String type;

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

    public static Notification fromRequest(NotificationCreateRequest request) {
        Notification notification = new Notification();
        notification
        return null;
    }

    public void markRead() {
        if (!this.isRead) {
            this.isRead = true;
            this.readAt = OffsetDateTime.now();
        }
    }

}
