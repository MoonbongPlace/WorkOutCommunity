package com.community.workout.api.dto.response;

import com.community.workout.domain.model.WorkOutLog;

import java.time.LocalDate;
import java.util.List;

public record WorkOutLogTodayResponse(
        Long workoutLogId,
        LocalDate logDate,
        String status,
        List<Item> items
) {
    public record Item(
            int orderSeq,
            Long exerciseId,
            String exerciseName,
            int sets,
            String reps,
            Integer rpe,
            Integer restSec,
            String notes
    ) {}

    public static WorkOutLogTodayResponse from(WorkOutLog log) {
        return new WorkOutLogTodayResponse(
                log.getId(),
                log.getLogDate(),
                log.getStatus().name(),
                log.getItems().stream()
                        .map(i -> new Item(
                                i.getOrderSeq(),
                                i.getExercise().getId(),
                                i.getExercise().getName(),
                                i.getPlannedSets(),
                                i.getPlannedReps(),
                                i.getPlannedRpe(),
                                i.getPlannedRestSec(),
                                i.getNotes()
                        ))
                        .toList()
        );
    }
}