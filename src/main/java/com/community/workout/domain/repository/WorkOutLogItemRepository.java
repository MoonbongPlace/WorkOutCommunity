package com.community.workout.domain.repository;

import com.community.workout.domain.model.WorkOutLogItem;

import java.util.Optional;

public interface WorkOutLogItemRepository {
    Optional<WorkOutLogItem> findByIdAndLogId(Long itemId, Long id);

    int findMaxOrderSeqActive(Long id);

    Optional<WorkOutLogItem> findByIdAndLogIdAndDeletedAtIsNull(Long itemId, Long logId);

    int shiftLeftAfterDelete(Long logId, int deletedSeq);
}
