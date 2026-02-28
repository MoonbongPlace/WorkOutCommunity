package com.community.workout.api.dto.response;

import com.community.workout.application.dto.UpdateWorkOutLogResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class UpdateWorkOutLogResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private UpdateWorkOutLogResult updateWorkOutLogResult;

    public static UpdateWorkOutLogResponse from(UpdateWorkOutLogResult updateWorkOutLogResult, String message) {
        return new UpdateWorkOutLogResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                updateWorkOutLogResult
        );
    }
}
