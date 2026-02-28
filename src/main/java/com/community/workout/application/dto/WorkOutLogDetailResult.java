package com.community.workout.application.dto;

import com.community.workout.domain.model.WorkOutLog;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","memberId","logDate","createdAt","list"})
public class WorkOutLogDetailResult {
    private Long id;
    private Long memberId;
    private LocalDate logDate;
    private OffsetDateTime createdAt;

    private List<Item> list;

    @Getter
    @AllArgsConstructor
    @JsonPropertyOrder({"id","logId","exerciseId","exerciseName","orderSeq","plannedSets","plannedReps","plannedRpe","plannedRestSec", "notes"})
    public static class Item{
        private Long id;
        private Long logId;
        private Long exerciseId;
        private String exerciseName;
        private Integer orderSeq;
        private Integer plannedSets;
        private String plannedReps;
        private Integer plannedRpe;
        private Integer plannedRestSec;
        private String notes;
    }

    public static WorkOutLogDetailResult of(WorkOutLog workOutLog) {
        List<Item> mappedItem = workOutLog.getItems().stream()
                .map(item -> new Item(
                        item.getId(),
                        item.getLog().getId(),
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
        return new WorkOutLogDetailResult(
                workOutLog.getId(),
                workOutLog.getMemberId(),
                workOutLog.getLogDate(),
                workOutLog.getCreatedAt(),
                mappedItem
        );
    }
}
