package com.community.comment.domain.repository;

import com.community.comment.domain.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository {
    Comment save(Comment comment);
}
