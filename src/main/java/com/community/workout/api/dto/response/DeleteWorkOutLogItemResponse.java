package com.community.workout.api.dto.response;

import com.community.workout.application.dto.DeleteWorkOutLogItemResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class DeleteWorkOutLogItemResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DeleteWorkOutLogItemResult deleteWorkOutLogItemResult;

    public static DeleteWorkOutLogItemResponse from(DeleteWorkOutLogItemResult deleteWorkOutLogItemResult, String message) {
        return new DeleteWorkOutLogItemResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                deleteWorkOutLogItemResult
        );
    }
}
