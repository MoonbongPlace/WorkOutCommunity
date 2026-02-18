package com.community.notification.application;

import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.CommonException;
import com.community.global.ResponseCode;
import com.community.notification.api.dto.request.NotificationCreateRequest;
import com.community.notification.domain.model.Notification;
import com.community.notification.domain.model.NotificationType;
import com.community.notification.infra.persistence.NotificationRepositoryAdapter;
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
    private final PostRepositoryAdapter postRepositoryAdapter;

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
                .orElseThrow(() -> new CommonException(ResponseCode.NOTIFICATION_NOT_FOUND));

        if (!notification.getMemberId().equals(memberId)) {
            throw new CommonException(ResponseCode.NOTIFICATION_FORBIDDEN);
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

    @Transactional
    public NotificationCreateResult createNotificationFromApi(
            Long senderId, NotificationCreateRequest request) {

        Long receiverId = postRepositoryAdapter.findAuthorIdByPostId(request.getPostId())
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        // 자기 자신에게 알림 방지
        if (receiverId.equals(senderId)) {
            return NotificationCreateResult.skipped();
        }

        String linkUrl = "/posts/" + request.getPostId();

        Notification notification = Notification.of(
                receiverId, senderId, request.getPostId(), request.getType(), request.getMessage(), linkUrl
        );

        Notification saved = notificationRepositoryAdapter.save(notification);

        return NotificationCreateResult.of(saved);
    }

    @Transactional
    public void saveNotification(Long receiverId, Long senderId, Long postId,
                                 NotificationType type, String message, String linkUrl) {

        if (receiverId == null || senderId == null || postId == null || type == null) {
            throw new IllegalArgumentException("receiverId/senderId/postId/type must not be null");
        }
        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("message must not be blank");
        }
        // 자기 자신 알람 방지
        if (receiverId.equals(senderId)) {
            return;
        }

        // 링크 url 기본값 설정
        String finalLinkUrl = (linkUrl == null || linkUrl.isBlank())
                ? "/posts/" + postId
                : linkUrl;

        Notification notification = Notification.of(
                receiverId,
                senderId,
                postId,
                type,
                message,
                finalLinkUrl
        );

        notificationRepositoryAdapter.save(notification);
    }
}
