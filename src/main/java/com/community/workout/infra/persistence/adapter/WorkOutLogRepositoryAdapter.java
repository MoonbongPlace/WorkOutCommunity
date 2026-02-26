package com.community.workout.infra.persistence.adapter;

import com.community.workout.domain.model.WorkOutLog;
import com.community.workout.domain.repository.WorkOutLogRepository;
import com.community.workout.infra.persistence.WorkOutLogJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkOutLogRepositoryAdapter implements WorkOutLogRepository {
    private final WorkOutLogJpaRepository workOutLogJpaRepository;

    @Override
    public Optional<WorkOutLog> findByMemberIdAndLogDate(Long memberId, LocalDate logDate) {
        return workOutLogJpaRepository.findWithItemsAndExercise(memberId, logDate);
    }

    @Override
    public List<WorkOutLog> findAllByMemberIdOrderByLogDateDesc(Long memberId) {
            return workOutLogJpaRepository.findAllByMemberIdOrderByLogDateDesc(memberId);
    }

    @Override
    public WorkOutLog save(WorkOutLog log) {
        return workOutLogJpaRepository.save(log);
    }
}
