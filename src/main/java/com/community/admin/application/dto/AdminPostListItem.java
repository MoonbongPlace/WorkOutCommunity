package com.community.admin.application.dto;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","memberId","title","content","categoryId","image","visibility", "createdAt"})
public class AdminPostListItem {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long categoryId;
    private List<String> images;
    private PostVisibility visibility;
    private OffsetDateTime createdAt;

    public static AdminPostListItem from(Post post) {
        return new AdminPostListItem(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getImages(),
                post.getPostVisibility(),
                post.getCreatedAt()
        );
    }
}
