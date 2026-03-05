package com.community.board.application.dto;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","memberId","memberName","title","content","categoryId","images","visibility","createdAt","likeCount"})
public class PostListItem {
    private Long id;
    private Long memberId;
    private String memberName;
    private String title;
    private String content;
    private Long categoryId;
    private List<String> images;
    private PostVisibility visibility;
    private OffsetDateTime createdAt;
    private int likeCount;

    public static PostListItem from(Post post, String memberName) {
        return new PostListItem(
                post.getId(),
                post.getMemberId(),
                memberName,
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getImages(),
                post.getPostVisibility(),
                post.getCreatedAt(),
                post.getLikeCount()
        );
    }
}
