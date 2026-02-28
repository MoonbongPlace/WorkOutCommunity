package com.community.workout.api.dto.response;

import com.community.workout.application.dto.WorkOutLogListResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class WorkOutLogListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private WorkOutLogListResult workOutLogListResult;

    public static WorkOutLogListResponse from(WorkOutLogListResult workOutLogListResult, String message) {
        return new WorkOutLogListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                workOutLogListResult
        );
    }
}
