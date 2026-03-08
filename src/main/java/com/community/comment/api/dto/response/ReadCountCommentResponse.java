package com.community.comment.api.dto.response;

import com.community.comment.application.dto.ReadCountCommentResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ReadCountCommentResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private int readCountCommentResult;

    public static ReadCountCommentResponse from(int readCountCommentResult, String message) {
            return new ReadCountCommentResponse(
                    String.valueOf(HttpStatus.OK.value()),
                    message,
                    OffsetDateTime.now(),
                    readCountCommentResult
            );
    }
}
