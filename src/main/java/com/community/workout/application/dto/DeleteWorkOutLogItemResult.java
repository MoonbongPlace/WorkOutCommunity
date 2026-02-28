package com.community.workout.application.dto;

import com.community.workout.domain.model.WorkOutLogItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"workOutLogId","itemId","orderSeq","exerciseId","deletedAt"})
public class DeleteWorkOutLogItemResult {
    private final Long workOutLogId;
    private final Long itemId;
    private final int orderSeq;
    private final Long exerciseId;
    private final OffsetDateTime deletedAt;

    public static DeleteWorkOutLogItemResult of(WorkOutLogItem item) {
        return new DeleteWorkOutLogItemResult(
                item.getLog().getId(),
                item.getId(),
                item.getOrderSeq(),
                item.getExercise().getId(),
                item.getDeletedAt()
        );
    }
}
