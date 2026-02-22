package com.community.board.api.dto.response;

import com.community.board.application.dto.UpdatePostResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class UpdatePostResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private UpdatePostResult post;

    public static UpdatePostResponse update(UpdatePostResult updatedPost, String message) {
        return new UpdatePostResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                updatedPost
        );
    }
}
