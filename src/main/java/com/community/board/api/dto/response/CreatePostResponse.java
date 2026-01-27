package com.community.board.api.dto.response;

import com.community.board.application.dto.CreatePostResult;
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
public class CreatePostResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CreatePostResult post;

    public static CreatePostResponse create(CreatePostResult createdPost, String message) {
        return new CreatePostResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                createdPost
        );
    }
}
