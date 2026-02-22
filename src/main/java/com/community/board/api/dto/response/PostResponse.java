package com.community.board.api.dto.response;

import com.community.board.application.dto.DetailPostResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class PostResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DetailPostResult post;

    public static PostResponse get(DetailPostResult postDetailDTO, String message){
        return new PostResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                postDetailDTO
        );
    }


}