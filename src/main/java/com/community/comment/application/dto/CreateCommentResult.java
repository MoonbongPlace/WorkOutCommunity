package com.community.comment.application.dto;

import com.community.comment.domain.model.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "postId", "content", "createdAt"})
public class CreateCommentResult {

    private Long id;
    private Long memberId;
    private Long postId;
    private String content;
    private OffsetDateTime createdAt;

    public static CreateCommentResult from(Comment saved) {
        return new CreateCommentResult(
                saved.getId(),
                saved.getMemberId(),
                saved.getPostId(),
                saved.getContent(),
                saved.getCreatedAt()
        );
    }
}
