package com.community.workout.domain.repository;

import com.community.workout.domain.model.WorkOutLog;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkOutLogRepository {
    Optional<WorkOutLog> findByMemberIdAndLogDate(Long memberId, LocalDate logDate);
    List<WorkOutLog> findAllByMemberIdOrderByLogDateDesc(Long memberId);
    WorkOutLog save(WorkOutLog log);

    Optional<WorkOutLog> findById(Long workOutLogId);

    Optional<WorkOutLog> findByIdAndMemberId(Long memberId, Long workOutLogId);

    void delete(WorkOutLog log);

    Optional<WorkOutLog> findByIdAndMemberIdAndDeletedAtIsNull(Long workOutLogId, Long memberId);

    Optional<WorkOutLog> findDetailActive(Long Id, Long memberId);

    boolean existsByMemberIdAndLogDateAndDeletedAtIsNull(Long memberId,LocalDate logDate);

}
