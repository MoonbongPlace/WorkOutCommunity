package com.community.workout.api.dto.response;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.community.workout.domain.model.Exercise;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ExerciseListResponse {

    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private List<ExerciseSummary> exercises;

    @Getter
    @AllArgsConstructor
    public static class ExerciseSummary {
        private Long id;
        private String name;
        private List<String> bodyParts;
    }

    public static ExerciseListResponse of(List<Exercise> exercises, ObjectMapper objectMapper) {
        List<ExerciseSummary> summaries = exercises.stream()
                .map(e -> {
                    List<String> parts;
                    try {
                        parts = objectMapper.readValue(e.getBodyPartsJson(), new TypeReference<>() {});
                    } catch (Exception ex) {
                        parts = List.of();
                    }
                    return new ExerciseSummary(e.getId(), e.getName(), parts);
                })
                .toList();

        return new ExerciseListResponse("200", "운동 목록 조회 성공", OffsetDateTime.now(), summaries);
    }
}
