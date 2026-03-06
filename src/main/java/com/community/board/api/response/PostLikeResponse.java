package com.community.board.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PostLikeResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private boolean checkPostLike;

    public static PostLikeResponse of(boolean checkPostLike, String message) {
        return new PostLikeResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                checkPostLike
        );
    }
}
