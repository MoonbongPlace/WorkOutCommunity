package com.community.workout.api.dto.response;

import com.community.workout.application.dto.ReplaceExerciseResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class ReplaceExerciseResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private ReplaceExerciseResult replaceExerciseResult;

    public static ReplaceExerciseResponse from(ReplaceExerciseResult replaceExerciseResult, String message) {
        return new ReplaceExerciseResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                replaceExerciseResult
        );
    }
}
