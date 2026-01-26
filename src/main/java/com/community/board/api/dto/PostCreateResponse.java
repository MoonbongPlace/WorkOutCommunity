package com.community.board.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCreateResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private PostCreateResult post;

    public static PostCreateResponse create(PostCreateResult createdPost, String message) {
        return new PostCreateResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                createdPost
        );
    }
}
