package com.community.comment.infra.persistance;

import com.community.comment.domain.model.Comment;
import com.community.comment.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryAdapter implements CommentRepository {
    private final CommentJpaRepository commentJpaRepository;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentJpaRepository.findById(commentId);
    }

    @Override
    public List<Comment> findActiveByPostId(Long postId) {
        return commentJpaRepository.findActiveByPostId(postId);
    }

    @Override
    public int countByPostId(Long postId) {
        return commentJpaRepository.countByPostId(postId);
    }

    @Override
    public int countActiveByPostId(Long postId) {
        return commentJpaRepository.countByPostIdAndDeletedAtIsNull(postId);
    }
}
