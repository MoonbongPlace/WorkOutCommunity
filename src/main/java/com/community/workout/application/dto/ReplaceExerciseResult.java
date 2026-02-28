package com.community.workout.application.dto;

import com.community.workout.domain.model.WorkOutLogItem;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","logId","exerciseId","updatedAt"})
public class ReplaceExerciseResult {
    private Long id;
    private Long logId;
    private Long exerciseId;
    private OffsetDateTime updatedAt;

    public static ReplaceExerciseResult of(WorkOutLogItem replacedItem) {
        return new ReplaceExerciseResult(
                replacedItem.getId(),
                replacedItem.getLog().getId(),
                replacedItem.getExercise().getId(),
                replacedItem.getUpdatedAt()
        );
    }
}
