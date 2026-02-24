package com.community.workout.infra.persistence;

import com.community.workout.domain.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExerciseJpaRepository extends JpaRepository<Exercise, Long> {
    Optional<Exercise> findByName(String name);

    List<Exercise> findTop5ByNameContainingIgnoreCaseOrderByNameAsc(String name);

    List<Exercise> findTop5ByNameContainingIgnoreCase(String name);
}
