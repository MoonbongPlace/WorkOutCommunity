package com.community.board.infra.persistence;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("select p.memberId from Post p where p.id = :postId")
    Optional<Long> findMemberIdByPostId(@Param("postId") Long postId);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.id = :id
      AND p.deletedAt IS NULL
      AND p.postVisibility = com.community.board.domain.model.PostVisibility.VISIBLE
""")
    Optional<Post> findActiveVisibleById(@Param("id") Long id);

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.deletedAt IS NULL
      AND p.postVisibility = :visibility
    ORDER BY p.createdAt DESC
""")
    Page<Post> findAllActiveByVisibility(
            @Param("visibility") PostVisibility visibility,
            Pageable pageable
    );

    @Query("""
    SELECT p
    FROM Post p
    WHERE p.memberId = :memberId
      AND p.deletedAt IS NULL
    ORDER BY p.createdAt DESC
""")
    Page<Post> findAllByMemberId(
            @Param("memberId") Long memberId,
            Pageable pageable
    );

    Page<Post> findAllByDeletedAtIsNullAndPostVisibilityAndCategoryId(
            PostVisibility postVisibility,
            Long categoryId,
            Pageable pageable
    );

    @Query("""
    SELECT p.title
    FROM Post p
    WHERE p.id = :postId
      AND p.deletedAt IS NULL
    """)
    Optional<String> findPostTitleByIdAndDeletedAt(@Param("postId") Long postId);

    boolean existsByIdAndDeletedAtIsNull(@Param("postId") Long postId);
}