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

    List<WorkOutLog> findAllByMemberIdOrderByLogDateDesc(Long memberId);

    Optional<WorkOutLog> findByMemberIdAndId(Long workOutLogId, Long memberId);

    Optional<WorkOutLog> findByIdAndMemberIdAndDeletedAtIsNull(Long workOutLogId, Long memberId);
}
