package com.community.workout.api;

import com.community.workout.api.dto.response.ExerciseListResponse;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Exercise", description = "운동 목록 API")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/exercises")
public class ExerciseController {

    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<ExerciseListResponse> getAll() {
        return ResponseEntity.ok(
                ExerciseListResponse.of(exerciseRepositoryAdapter.findAll(), objectMapper)
        );
    }
}
