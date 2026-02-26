package com.community.workout.api.dto.response;

import com.community.workout.application.CreateWorkOutLogResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CreateWorkOutLogResponse{
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CreateWorkOutLogResult createWorkOutLogResult;

    public static CreateWorkOutLogResponse from(CreateWorkOutLogResult createWorkOutLogResult, String message) {
        return new CreateWorkOutLogResponse(
                String.valueOf(HttpStatus.CREATED.value()),
                message,
                OffsetDateTime.now(),
                createWorkOutLogResult
        );
    }
}
