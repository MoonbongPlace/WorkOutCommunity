package com.community.comment.infra.persistance;

import com.community.comment.application.dto.PostCommentCountRow;
import com.community.comment.domain.model.Comment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.deletedAt IS NULL ORDER BY c.createdAt ASC")
    List<Comment> findActiveByPostId(@Param("postId") Long postId);

    int countByPostId(Long postId);

    int countByPostIdAndDeletedAtIsNull(Long postId);

    @Query("""
                select new com.community.comment.application.dto.PostCommentCountRow(c.postId, count(c))
                from Comment c
                where c.postId in :postIds
                  and c.deletedAt is null
                group by c.postId
            """)
    List<PostCommentCountRow> countActiveByPostIds(@Param("postIds") List<Long> postIds);
}
