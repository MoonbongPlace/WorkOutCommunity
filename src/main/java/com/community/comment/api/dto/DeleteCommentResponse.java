package com.community.comment.api.dto;

import com.community.comment.application.DeleteCommentResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCommentResponse {
    private String code;
    private String message;
    private OffsetDateTime timeStamp;
    private DeleteCommentResult deleteCommentResult;

    public static DeleteCommentResponse from(DeleteCommentResult deleteCommentResult, String message){
        return new DeleteCommentResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                deleteCommentResult
        );
    }

}
