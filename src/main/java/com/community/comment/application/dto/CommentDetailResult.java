package com.community.comment.application.dto;

import com.community.comment.domain.model.Comment;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "memberName", "content", "createdAt","profileImage"})
public class CommentDetailResult {

    private Long id;
    private Long memberId;
    private String memberName;
    private String content;
    private OffsetDateTime createdAt;
    private String profileImage;

    public static CommentDetailResult from(Comment comment, String memberName, String profileImage) {
        return new CommentDetailResult(
                comment.getId(),
                comment.getMemberId(),
                memberName,
                comment.getContent(),
                comment.getCreatedAt(),
                profileImage
        );
    }
}
