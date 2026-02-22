package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","memberId","title","content","categoryId","image","visibility","createdAt"})
public class PostListItem {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long categoryId;
    private String image;
    private PostVisibility visibility;
    private OffsetDateTime createdAt;

    public static PostListItem from(Post post) {
        return new PostListItem(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getImage(),
                post.getPostVisibility(),
                post.getCreatedAt()
        );
    }
}
