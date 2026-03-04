package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "title", "content", "categoryId", "views", "images", "createdAt"})
public class DetailPostResult {
    private Long id;
    private Long member_id;
    private String title;
    private String content;
    private Long categoryId;
    private int views;
    private List<String> images;
    private OffsetDateTime createdAt;

    public static DetailPostResult from(Post post){
        return new DetailPostResult(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getViews(),
                post.getImages(),
                post.getCreatedAt()
        );
    }
}
