package com.community.comment.application.dto;

import com.community.comment.domain.model.Comment;
import com.community.notification.application.dto.CreateNotificationCommentResult;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "postId", "content", "createdAt", "createNotificationCommentResult"})
public class CreateCommentResult {

    private Long id;
    private Long memberId;
    private Long postId;
    private String content;
    private OffsetDateTime createdAt;
    private CreateNotificationCommentResult createNotificationCommentResult;

    public static CreateCommentResult from(Comment saved, CreateNotificationCommentResult createNotificationCommentResult) {
        return new CreateCommentResult(
                saved.getId(),
                saved.getMemberId(),
                saved.getPostId(),
                saved.getContent(),
                saved.getCreatedAt(),
                createNotificationCommentResult
        );
    }
}
