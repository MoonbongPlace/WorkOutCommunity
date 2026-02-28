package com.community.workout.domain.repository;

import com.community.workout.domain.model.WorkOutLogItem;

import java.util.Optional;

public interface WorkOutLogItemRepository {
    Optional<WorkOutLogItem> findByIdAndLogId(Long itemId, Long id);
}
