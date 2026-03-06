package com.community.board.infra.persistence;

import com.community.board.domain.model.PostLike;
import com.community.board.domain.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PostLikeRepositoryAdapter implements PostLikeRepository {
    private final PostLikeJpaRepository postLikeJpaRepository;

    @Override
    public PostLike save(PostLike postLike) {
        return postLikeJpaRepository.save(postLike);
    }

    @Override
    public PostLike findByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeJpaRepository.findByMemberIdAndPostId(memberId, postId);
    }

    @Override
    public void delete(PostLike postLike) {
        postLikeJpaRepository.delete(postLike);
    }

    @Override
    public boolean existsByMemberIdAndPostId(Long memberId, Long postId) {
        return postLikeJpaRepository.existsByMemberIdAndPostId(memberId, postId);
    }
}
