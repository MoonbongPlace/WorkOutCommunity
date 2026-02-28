package com.community.workout.api.dto.response;

import com.community.workout.application.dto.WorkOutLogDetailResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class WorkOutLogDetailResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private WorkOutLogDetailResult workOutLogDetailResult;

    public static WorkOutLogDetailResponse from(WorkOutLogDetailResult workOutLogDetailResult, String message) {
        return new WorkOutLogDetailResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                workOutLogDetailResult
        );
    }
}
