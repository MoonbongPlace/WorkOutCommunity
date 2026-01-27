package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "title", "content", "categoryId", "views", "image", "deletedAt"})
public class DeletePostResult {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long categoryId;
    private int views;
    private String image;
    private OffsetDateTime deletedAt;

    public static DeletePostResult from(Post post) {
        return new DeletePostResult(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getViews(),
                post.getImage(),
                post.getDeletedAt()
        );
    }
}
