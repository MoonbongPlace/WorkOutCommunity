package com.community.workout.domain.repository;

import com.community.workout.domain.model.WorkOutLog;

import java.time.LocalDate;
import java.util.Optional;

public interface WorkOutLogRepository {
    Optional<WorkOutLog> findByMemberIdAndLogDate(Long memberId, LocalDate logDate);

    WorkOutLog save(WorkOutLog log);
}
