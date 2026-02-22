package com.community.comment.application.dto;

import com.community.comment.domain.model.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "postId", "content", "deletedAt"})
public class DeleteCommentResult {
    private Long id;
    private Long memberId;
    private Long postId;
    private String content;
    private OffsetDateTime deletedAt;

    public static DeleteCommentResult from(Comment comment) {
        return new DeleteCommentResult(
                comment.getId(),
                comment.getMemberId(),
                comment.getPostId(),
                comment.getContent(),
                comment.getDeletedAt()
        );
    }
}
