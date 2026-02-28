package com.community.workout.api.dto.response;

import com.community.workout.application.dto.DeleteWorkOutLogResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class DeleteWorkOutLogResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DeleteWorkOutLogResult deleteWorkOutLogResult;

    public static DeleteWorkOutLogResponse from(DeleteWorkOutLogResult deleteWorkOutLogResult, String message) {
        return new DeleteWorkOutLogResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                deleteWorkOutLogResult
        );
    }
}
