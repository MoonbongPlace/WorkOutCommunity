package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "title", "content", "categoryId", "views", "image", "createdAt"})
public class UpdatePostResult {

    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long categoryId;
    private int views;
    private String image;
    private OffsetDateTime createdAt;

    public static UpdatePostResult from(Post post){
        return new UpdatePostResult(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getViews(),
                post.getImage(),
                post.getCreatedAt()
        );
    }
}
