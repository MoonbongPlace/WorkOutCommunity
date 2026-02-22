package com.community.board.api.dto.response;

import com.community.board.application.dto.DeletePostResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class DeletePostResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DeletePostResult post;

    public static DeletePostResponse delete(DeletePostResult deletedPost, String message) {
        return new DeletePostResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                deletedPost
        );
    }
}
