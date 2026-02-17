package com.community.board.infra.persistence;

import com.community.board.domain.model.Post;
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
}