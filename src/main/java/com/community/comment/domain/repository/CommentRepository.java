package com.community.comment.domain.repository;

import com.community.comment.domain.model.Comment;

import java.util.Optional;

public interface CommentRepository {
    Comment save(Comment comment);
    Optional<Comment> findById(Long commentId);
}
