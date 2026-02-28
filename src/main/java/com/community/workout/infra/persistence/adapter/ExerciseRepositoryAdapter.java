package com.community.workout.infra.persistence.adapter;

import com.community.workout.domain.model.Exercise;
import com.community.workout.domain.repository.ExerciseRepository;
import com.community.workout.infra.persistence.ExerciseJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ExerciseRepositoryAdapter implements ExerciseRepository {
    private final ExerciseJpaRepository exerciseJpaRepository;


    @Override
    public Optional<Exercise> findByName(String name) {
        return exerciseJpaRepository.findByName(name);
    }

    @Override
    public List<Exercise> findTop5ByNameContainingIgnoreCaseOrderByNameAsc(String name) {
        return exerciseJpaRepository.findTop5ByNameContainingIgnoreCaseOrderByNameAsc(name);
    }

    public List<Exercise> findAllById(Collection<Long> ids) {
        return exerciseJpaRepository.findAllById(ids);
    }

    @Override
    public List<Exercise> findTop5ByNameContainingIgnoreCase(String keyword) {
        return exerciseJpaRepository.findTop5ByNameContainingIgnoreCase(keyword);
    }

    @Override
    public Optional<Exercise> findById(Long exerciseId) {
        return exerciseJpaRepository.findById(exerciseId);
    }

    @Override
    public Exercise save(Exercise exercise) {
        return exerciseJpaRepository.save(exercise);
    }

}
