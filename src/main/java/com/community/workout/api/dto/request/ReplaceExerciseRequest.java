package com.community.workout.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReplaceExerciseRequest {
    @NotNull(message = "운동 교체 시 운동 필수 기입")
    private Long newExerciseId;
}
