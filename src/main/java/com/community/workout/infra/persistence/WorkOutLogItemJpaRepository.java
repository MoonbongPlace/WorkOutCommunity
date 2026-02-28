package com.community.workout.infra.persistence;

import com.community.workout.domain.model.WorkOutLogItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WorkOutLogItemJpaRepository extends JpaRepository<WorkOutLogItem, Long> {
    Optional<WorkOutLogItem> findByIdAndLogId(Long itemId, Long id);

    @Query("""
            select coalesce(max(i.orderSeq), 0)
            from WorkOutLogItem i
            where i.log.id = :logId
              and i.deletedAt is null
            """)
    int findMaxOrderSeqActive(@Param("logId") Long logId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update WorkOutLogItem i
            set i.orderSeq = i.orderSeq - 1
            where i.log.id = :logId
              and i.deletedAt is null
              and i.orderSeq > :deletedOrderSeq
            """)
    int shiftLeftAfterDelete(@Param("logId") Long logId,
                             @Param("deletedOrderSeq") int deletedOrderSeq);

    Optional<WorkOutLogItem> findByIdAndLogIdAndDeletedAtIsNull(Long itemId, Long logId);
}
