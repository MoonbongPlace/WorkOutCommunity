package com.community.workout.infra.persistence;

import com.community.workout.domain.model.WorkOutLogItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkOutLogItemJpaRepository extends JpaRepository<WorkOutLogItem, Long> {
    Optional<WorkOutLogItem> findByIdAndLogId(Long itemId, Long id);
}
