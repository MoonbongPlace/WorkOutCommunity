package com.community.chatbot.api.dto.response;

import java.time.OffsetDateTime;
import java.util.List;

public record ChatMessageResponse(
        String answer,
        OffsetDateTime createdAt,
        ChatMode mode,                 // COACH / ROUTINE
        RoutineDraft routineDraft
) {
    public enum ChatMode { COACH, ROUTINE }

    public record RoutineDraft(
            List<RoutineItem> items
    ) {}

    public record RoutineItem(
            Long exerciseId,
            Integer orderSeq,
            Integer sets,
            String reps,
            Integer rpe,
            Integer restSec,
            String notes
    ) {}
}