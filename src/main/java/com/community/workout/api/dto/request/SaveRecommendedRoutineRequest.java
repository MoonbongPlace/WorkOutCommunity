package com.community.workout.api.dto.request;

import jakarta.validation.constraints.*;

import java.util.List;

public record SaveRecommendedRoutineRequest(
        @NotEmpty List<Item> items
) {
    public record Item(
            @NotNull Long exerciseId,
            @Min(1) @Max(999) int orderSeq,
            @Min(1) @Max(12) int sets,
            @NotBlank @Size(max = 20) String reps,
            @Min(1) @Max(10) Integer rpe,
            @Min(0) @Max(600) Integer restSec,
            @Size(max = 1000) String notes
    ) {}
}