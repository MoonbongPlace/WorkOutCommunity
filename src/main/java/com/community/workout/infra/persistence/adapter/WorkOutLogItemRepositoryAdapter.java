package com.community.workout.infra.persistence.adapter;

import com.community.workout.domain.model.WorkOutLogItem;
import com.community.workout.domain.repository.WorkOutLogItemRepository;
import com.community.workout.infra.persistence.WorkOutLogItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WorkOutLogItemRepositoryAdapter implements WorkOutLogItemRepository {
    private final WorkOutLogItemJpaRepository workOutLogItemJpaRepository;

    @Override
    public Optional<WorkOutLogItem> findByIdAndLogId(Long itemId, Long id) {
            return workOutLogItemJpaRepository.findByIdAndLogId(itemId, id);
    }

    @Override
    public int findMaxOrderSeqActive(Long id) {
        return workOutLogItemJpaRepository.findMaxOrderSeqActive(id);
    }

    @Override
    public Optional<WorkOutLogItem> findByIdAndLogIdAndDeletedAtIsNull(Long itemId, Long logId) {
        return workOutLogItemJpaRepository.findByIdAndLogIdAndDeletedAtIsNull(itemId, logId);
    }

    @Override
    public int shiftLeftAfterDelete(Long logId, int deletedSeq) {
        return workOutLogItemJpaRepository.shiftLeftAfterDelete(logId, deletedSeq);
    }
}
