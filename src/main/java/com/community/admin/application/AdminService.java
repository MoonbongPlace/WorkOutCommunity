package com.community.admin.application;

import com.community.admin.api.dto.request.AdminMemberStatusUpdateRequest;
import com.community.admin.api.dto.request.AdminNotificationBroadcastRequest;
import com.community.admin.application.dto.*;
import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.CommonException;
import com.community.global.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import com.community.notification.domain.model.Notification;
import com.community.notification.domain.model.NotificationType;
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
public class AdminService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final NotificationRepositoryAdapter notificationRepositoryAdapter;

    // 관리자 : 회원 조회
    @Transactional(readOnly = true)
    public AdminMemberListResult memberList(Pageable pageable) {
        Page<Member> page = memberRepositoryAdapter.findAll(pageable);

        return AdminMemberListResult.from(page);
    }

    // 관리자 : 회원 상태 변경
    @Transactional
    public AdminMemberStatusUpdateResult updateMemberStatus(Long memberId, AdminMemberStatusUpdateRequest request) {
        Member member = memberRepositoryAdapter.findById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        if (member.getDeletedAt() != null && request.getStatus() != MemberStatus.DELETED) {
            throw new CommonException(ResponseCode.MEMBER_ALREADY_DELETED);
        }

        member.changeStatus(request.getStatus());
        if (request.getStatus()==MemberStatus.DELETED)
            member.setDeletedAt(OffsetDateTime.now());

        return AdminMemberStatusUpdateResult.from(member);
    }

    // 관리자 : 게시글 조회
    @Transactional(readOnly = true)
    public AdminPostListResult postList(Pageable pageable) {
        Page<Post> page = postRepositoryAdapter.findAll(pageable);

        return AdminPostListResult.from(page);
    }

    // 관리자 : 게시글 상세 조회
    @Transactional(readOnly = true)
    public AdminPostDetailResult detailPost(Long postId) {
        Post post = postRepositoryAdapter.findById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        return AdminPostDetailResult.from(post);
    }

    // 관리자 : 게시글 상태 변경
    @Transactional
    public AdminPostVisibilityUpdateResult updatePostVisibility(PostVisibility visibility, Long postId) {
        Post post = postRepositoryAdapter.findById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (post.getDeletedAt()!= null) {
            throw new CommonException(ResponseCode.POST_ALREADY_DELETED);
        }
        if (visibility == PostVisibility.HIDDEN) post.hide();
        else post.show();

        return AdminPostVisibilityUpdateResult.from(post);
    }

    @Transactional
    public long broadcast(Long adminId, @Valid AdminNotificationBroadcastRequest request) {
        List<Long> receiverIds = memberRepositoryAdapter.findAllActiveMemberIds();

        List<Notification> notifications = receiverIds.stream()
                .filter(id -> !id.equals(adminId))
                .map(receiverId-> Notification.createBroadcast(
                        receiverId,
                        adminId,
                        request.getMessage(),
                        request.getLinkUrl()
                ))
                .toList();

        notificationRepositoryAdapter.saveAll(notifications);
        return notifications.size();
    }
}
