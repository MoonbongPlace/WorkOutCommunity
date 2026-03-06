package com.community.board.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class DeletePostLikeResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;

    public static DeletePostLikeResponse of( String message) {
        return new DeletePostLikeResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now()
        );
    }
}
