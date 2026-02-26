package com.community.chatbot.application.dto;

import java.util.List;

public record RoutinePlan(
        String summary,
        String goal,
        int weeklyFrequency,
        String equipment,
        List<Exercise> exercises,
        String notes
) {
    public record Exercise(
            String name,
            int sets,
            String reps,
            int rpe,
            int restSec
    ) {}
}