package com.community.comment.api.dto.response;

import com.community.comment.application.dto.CommentListResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CommentListResult getCommentsResult;

    public static CommentListResponse from(CommentListResult result, String message) {
        return new CommentListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                result
        );
    }
}
