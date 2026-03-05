package com.community.board.application;

import com.community.board.domain.model.PostLike;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId","postId", "createdAt"})
public class CreatePostLikeResult {
    private Long id;
    private Long memberId;
    private Long postId;
    private OffsetDateTime createdAt;

    public static CreatePostLikeResult of(PostLike postLike) {
        return new CreatePostLikeResult(
                postLike.getId(),
                postLike.getMemberId(),
                postLike.getPostId(),
                postLike.getCreatedAt()
        );
    }
}
