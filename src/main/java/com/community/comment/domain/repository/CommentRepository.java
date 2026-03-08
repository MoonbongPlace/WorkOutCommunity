package com.community.comment.domain.repository;

import com.community.comment.domain.model.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long commentId);
    List<Comment> findActiveByPostId(Long postId);

    int countByPostId(Long postId);

    int countActiveByPostId(Long postId);
}
