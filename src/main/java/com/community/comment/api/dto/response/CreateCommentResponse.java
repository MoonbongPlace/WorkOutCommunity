package com.community.comment.api.dto.response;

import com.community.comment.application.dto.CreateCommentResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    CreateCommentResult createCommentResult;

    public static CreateCommentResponse from(CreateCommentResult createdComment, String message) {
        return new CreateCommentResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                createdComment
        );
    }
}
