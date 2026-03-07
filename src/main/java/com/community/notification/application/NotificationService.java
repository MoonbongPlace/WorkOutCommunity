package com.community.notification.application;

import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import com.community.notification.application.dto.*;
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
    private final MemberRepositoryAdapter memberRepositoryAdapter;

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

    // 댓글 작성 시 알림 생성.
    @Transactional
    public CreateNotificationCommentResult createNotificationComment(
            Long senderId, Long postId) {
        Long receiverId = postRepositoryAdapter.findAuthorIdByPostId(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        // 자기 자신에게 알림 방지
        if (receiverId.equals(senderId)) {
            return CreateNotificationCommentResult.skipped();
        }

        String memberName = memberRepositoryAdapter.findMemberNameByIdAndStatus(senderId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String postTitle = postRepositoryAdapter.findPostTitleById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        String message = memberName + "님이 '" + postTitle + "' 게시글에 댓글을 작성했습니다!";
        String linkUrl = "/posts/" + postId;

        Notification notification = Notification.createComment(
                receiverId,
                senderId,
                postId,
                message,
                linkUrl,
                NotificationType.COMMENT
        );

        Notification saved = notificationRepositoryAdapter.save(notification);

        return CreateNotificationCommentResult.of(saved);
    }

    // 게시글 좋아요 누르기 알림 생성.
    @Transactional
    public CreateNotificationPostLikeResult createNotificationPostLike(
            Long senderId, Long postId) {

        Long receiverId = postRepositoryAdapter.findAuthorIdByPostId(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        // 자기 자신에게 알림 방지
        if (receiverId.equals(senderId)) {
            return CreateNotificationPostLikeResult.skipped();
        }

        String memberName = memberRepositoryAdapter.findMemberNameByIdAndStatus(senderId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String postTitle = postRepositoryAdapter.findPostTitleById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));


        String message = memberName + "님이 '" + postTitle + "' 게시글에 좋아요을 눌렀습니다!";
        String linkUrl = "/posts/" + postId;

        Notification notification = Notification.createPostLike(
                receiverId,
                senderId,
                postId,
                message,
                linkUrl,
                NotificationType.POST_LIKE

        );

        Notification saved = notificationRepositoryAdapter.save(notification);

        return CreateNotificationPostLikeResult.of(saved);
    }
}
