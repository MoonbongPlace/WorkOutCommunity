package com.community.admin.application;

import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"postId", "postVisibility","reason"})
public class AdminPostVisibilityUpdateResult {
    private Long postId;
    private PostVisibility postVisibility;
    private OffsetDateTime timestamp;

    public static AdminPostVisibilityUpdateResult from(Post post) {
        return new AdminPostVisibilityUpdateResult(
                post.getId(),
                post.getPostVisibility(),
                OffsetDateTime.now()
        );
    }
}
