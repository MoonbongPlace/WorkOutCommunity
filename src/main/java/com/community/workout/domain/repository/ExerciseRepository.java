package com.community.workout.domain.repository;

import com.community.workout.domain.model.Exercise;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ExerciseRepository {
    List<Exercise> findAll();

    Optional<Exercise> findByName(String name);
    List<Exercise> findTop5ByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Exercise> findAllById(Collection<Long> ids);

    List<Exercise> findTop5ByNameContainingIgnoreCase(String keyword);

    Optional<Exercise> findById(@NotNull Long exerciseId);

    Exercise save(Exercise squat);
}
