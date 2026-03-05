package com.community.board.application;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostLike;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class postLikeService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PostRepositoryAdapter postRepositoryAdapter;

    @Transactional
    public CreatePostLikeResult createLike(Long memberId, Long postId) {
        memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        Post post = postRepositoryAdapter.findActiveVisibleById(postId)
                .orElseThrow(() -> new CommonException(ResponseCode.POST_NOT_FOUND));

        PostLike postLike = PostLike.create(memberId, postId);

        post.increaseLikeCount();

        return CreatePostLikeResult.of(postLike);
    }
}
