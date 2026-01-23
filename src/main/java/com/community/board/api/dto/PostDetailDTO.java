package com.community.board.api.dto;

import com.community.board.domain.model.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "body", "categoryId", "views", "authorId", "image", "createdAt"})
public class PostDetailDTO {
    private Long id;
    private String title;
    private String body;
    private Long categoryId;
    private int views;
    private Long authorId;
    private String image;
    private OffsetDateTime createdAt;

    public static PostDetailDTO from(Post post){
        return new PostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getViews(),
                post.getMemberId(),
                post.getImage(),
                post.getCreatedAt()
        );
    }
}
