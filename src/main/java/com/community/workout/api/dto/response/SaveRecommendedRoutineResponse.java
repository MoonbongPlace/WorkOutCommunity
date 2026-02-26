package com.community.workout.api.dto.response;

public record SaveRecommendedRoutineResponse(
        Long workoutLogId,
        String message
) {
    public static SaveRecommendedRoutineResponse of(Long id) {
        return new SaveRecommendedRoutineResponse(id, "추천 루틴 저장 성공");
    }
}