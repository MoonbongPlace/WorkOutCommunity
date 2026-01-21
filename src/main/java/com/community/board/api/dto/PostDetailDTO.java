package com.community.board.api.dto;

import com.community.board.domain.model.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "title", "body", "category", "views", "authorId", "image", "createdAt"})
public class PostDetailDTO {
    private Long id;
    private String title;
    private String body;
    private String category;
    private Long views;
    private Long authorId;
    private String image;
    private OffsetDateTime createdAt;

    public static PostDetailDTO from(Post post){
        return new PostDetailDTO(
                post.getId(),
                post.getTitle(),
                post.getBody(),
                post.getCategory(),
                post.getViews(),
                post.getAuthorId(),
                post.getImage(),
                post.getCreatedAt()
        );
    }
}
