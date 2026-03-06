package com.community.board.domain.repository;

import com.community.board.domain.model.PostLike;

public interface PostLikeRepository {
    PostLike save(PostLike postLike);

    PostLike findByMemberIdAndPostId(Long memberId, Long postId);

    void delete(PostLike postLike);

    boolean existsByMemberIdAndPostId(Long memberId, Long postId);
}
