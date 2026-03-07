package com.community.board.application;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostLike;
import com.community.board.infra.persistence.PostLikeRepositoryAdapter;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import com.community.notification.application.dto.CreateNotificationPostLikeResult;
import com.community.notification.application.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PostRepositoryAdapter postRepositoryAdapter;
    private final PostLikeRepositoryAdapter postLikeRepositoryAdapter;
    private final NotificationService notificationService;

    @Transactional
    public CreatePostLikeResult createLike(Long memberId, Long postId) {
        memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Post post = postRepositoryAdapter.findActiveVisibleById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        PostLike postLike = PostLike.create(memberId, postId);
        PostLike savedPostLike = postLikeRepositoryAdapter.save(postLike);

        // todo: 알림 후처리 이벤트 리스너 도입 예정 v2
        CreateNotificationPostLikeResult createNotificationPostLikeResult =
                notificationService.createNotificationPostLike(memberId, post.getMemberId());

        post.increaseLikeCount();

        return CreatePostLikeResult.of(savedPostLike, createNotificationPostLikeResult);
    }

    @Transactional
    public void deleteLike(Long memberId, Long postId) {
        memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Post post = postRepositoryAdapter.findActiveVisibleById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        PostLike postLike = postLikeRepositoryAdapter.findByMemberIdAndPostId(memberId, postId);

        postLike.validateDelete(memberId);

        postLikeRepositoryAdapter.delete(postLike);
        post.decreaseLikeCount();
    }

    public boolean checkPostLike(Long memberId, Long postId) {
        memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        postRepositoryAdapter.findActiveById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        return postLikeRepositoryAdapter.existsByMemberIdAndPostId(memberId, postId);
    }
}
