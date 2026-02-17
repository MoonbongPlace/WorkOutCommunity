package com.community.notification.infra;

import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.CommonException;
import com.community.global.ResponseCode;
import com.community.notification.application.NotificationService;
import com.community.notification.domain.model.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationEventHandler {
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(CommentCreatedEvent event) {

        Long receiverId = postRepositoryAdapter.findAuthorIdByPostId(event.postId())
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Long senderId = event.commenterId();

        // 자기 글에 자기 댓글이면 알림 생성 안하는 정책(선택)
        if (receiverId.equals(senderId)) return;

        notificationService.saveNotification(
                receiverId,
                senderId,
                event.postId(),
                NotificationType.COMMENT,
                event.message(),
                "/posts/" + event.postId()
        );
    }
}
