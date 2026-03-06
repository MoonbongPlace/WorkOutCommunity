package com.community.board.api.dto.response;

import com.community.board.application.CreatePostLikeResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CreatePostLikeResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CreatePostLikeResult createPostLikeResult;


    public static CreatePostLikeResponse from(CreatePostLikeResult createPostLikeResult, String message) {
        return new CreatePostLikeResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                createPostLikeResult
        );
    }
}
