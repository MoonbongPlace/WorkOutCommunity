package com.community.admin.application.dto;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminPostDetailResult {
    private Long id;
    private Long memberId;
    private String title;
    private String content;
    private Long categoryId;
    private String image;
    private PostVisibility visibility;
    private OffsetDateTime createdAt;
    private OffsetDateTime deletedAt;

    public static AdminPostDetailResult from(Post post) {
        return new AdminPostDetailResult(
                post.getId(),
                post.getMemberId(),
                post.getTitle(),
                post.getContent(),
                post.getCategoryId(),
                post.getImage(),
                post.getPostVisibility(),
                post.getCreatedAt(),
                post.getDeletedAt()
        );
    }
}
