package com.community.workout.infra.persistence;

import com.community.workout.domain.model.WorkOutLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkOutLogJpaRepository extends JpaRepository<WorkOutLog, Long> {
    @Query("""
            select l from WorkOutLog l
            left join fetch l.items i
            left join fetch i.exercise
            where l.memberId = :memberId and l.logDate = :date
            """)
    Optional<WorkOutLog> findWithItemsAndExercise(Long memberId, LocalDate date);

    List<WorkOutLog> findAllByMemberIdAndDeletedAtIsNullOrderByLogDateDesc(Long memberId);

    Optional<WorkOutLog> findByIdAndMemberId(Long memberId, Long workOutLogId);

    Optional<WorkOutLog> findByIdAndMemberIdAndDeletedAtIsNull(Long workOutLogId, Long memberId);

    @Query("""
            select distinct l from WorkOutLog l
            left join fetch l.items i
            where l.id = :logId
              and l.memberId = :memberId
              and l.deletedAt is null
              and (i is null or i.deletedAt is null)
            """)
    Optional<WorkOutLog> findDetailActive(Long logId, Long memberId);

    boolean existsByMemberIdAndLogDateAndDeletedAtIsNull(Long memberId, LocalDate logDate);
}
