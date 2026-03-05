package com.community.comment.infra.persistance;

import com.community.comment.domain.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findActiveByPostId(@Param("postId") Long postId);
}
