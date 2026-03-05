package com.community.board.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Table(name = "post_like")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "created_at",nullable = false)
    private OffsetDateTime createdAt;

    public static PostLike create(Long memberId, Long postId) {
        PostLike postLike = new PostLike();
        postLike.memberId = memberId;
        postLike.postId = postId;
        postLike.createdAt = OffsetDateTime.now();
        return postLike;
    }
}
