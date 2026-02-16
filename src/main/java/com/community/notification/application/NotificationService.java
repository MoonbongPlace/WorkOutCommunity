package com.community.notification.application;

import com.community.notification.api.dto.request.NotificationCreateRequest;
import com.community.notification.domain.model.Notification;
import com.community.notification.infra.persistence.NotificationRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepositoryAdapter notificationRepositoryAdapter;

    @Transactional(readOnly = true)
    public MyNotificationsResult getMyNotifications(Long memberId, Pageable pageable) {

        Page<Notification> page = notificationRepositoryAdapter
                .findByMemberIdOrderByCreatedAtDesc(memberId, pageable);

        long unreadCount = notificationRepositoryAdapter.countByMemberIdAndIsReadFalse(memberId);

        List<MyNotificationsResult.NotificationItem> items = page.getContent().stream()
                .map(MyNotificationsResult.NotificationItem::from)
                .toList();

        return new MyNotificationsResult(
                items,
                MyNotificationsResult.PageInfo.from(page),
                unreadCount
        );
    }

    @Transactional(readOnly = true)
    public UnReadCountResult getUnreadCount(Long memberId) {
        long count = notificationRepositoryAdapter.countByMemberIdAndIsReadFalse(memberId);

        return UnReadCountResult.of(count);
    }

    @Transactional
    public ReadOneResult readNotification(Long memberId, Long id) {

        Notification notification = notificationRepositoryAdapter.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));

        if (!notification.getMemberId().equals(memberId)) {
            throw new SecurityException("Forbidden");
        }

        notification.markRead();

        return ReadOneResult.of(
                notification.getId(),
                notification.getPostId(),
                notification.isRead(),
                notification.getReadAt()
        );
    }

    @Transactional
    public int readAllNotification(Long memberId) {
        return notificationRepositoryAdapter.markAllRead(memberId, OffsetDateTime.now());
    }

    public NotificationCreateResult createNotification(@Valid NotificationCreateRequest request) {
        Notification notification = Notification.fromRequest(request);
        return NotificationCreateResult.of(request);
    }
}
