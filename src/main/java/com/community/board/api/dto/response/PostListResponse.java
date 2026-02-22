package com.community.board.api.dto.response;

import com.community.board.application.dto.PostListResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PostListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private PostListResult postListResult;

    public static PostListResponse from(PostListResult postListResult, String message) {
        return new PostListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                postListResult
        );
    }
}
