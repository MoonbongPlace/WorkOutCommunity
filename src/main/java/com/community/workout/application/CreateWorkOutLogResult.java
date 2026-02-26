package com.community.workout.application;

import com.community.workout.domain.model.WorkOutLog;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberId", "logDate", "status", "createdAt", "exercises"})
public class CreateWorkOutLogResult {
    private Long id;
    private Long memberId;
    private LocalDate logDate;
    private WorkOutLog.Status status;
    private OffsetDateTime createdAt;
    private List<Item> exercises;

    @Getter
    @AllArgsConstructor
    @JsonPropertyOrder(
            {"itemId", "exerciseId", "exerciseName","orderSeq","plannedSets",
                    "plannedReps", "plannedRpe", "plannedRestSec", "notes"
            })
    public static class Item {
        private final Long itemId;
        private final Long exerciseId;
        private final String exerciseName;
        private final int orderSeq;
        private final int plannedSets;
        private final String plannedReps;
        private final Integer plannedRpe;
        private final Integer plannedRestSec;
        private final String notes;
    }

    public static CreateWorkOutLogResult of(WorkOutLog saved) {
        List<Item> mappedItems = saved.getItems().stream()
                .map(item -> new Item(
                        item.getId(),
                        item.getExercise().getId(),
                        item.getExercise().getName(),
                        item.getOrderSeq(),
                        item.getPlannedSets(),
                        item.getPlannedReps(),
                        item.getPlannedRpe(),
                        item.getPlannedRestSec(),
                        item.getNotes()
                ))
                .toList();

        return new CreateWorkOutLogResult(
                saved.getId(),
                saved.getMemberId(),
                saved.getLogDate(),
                saved.getStatus(),
                saved.getCreatedAt(),
                mappedItems
        );
    }
}
