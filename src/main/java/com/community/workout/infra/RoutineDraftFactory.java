package com.community.workout.infra;

import com.community.chatbot.api.dto.response.ChatMessageResponse;
import com.community.chatbot.application.dto.RoutinePlan;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.workout.domain.model.Exercise;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RoutineDraftFactory {

    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;

    public ChatMessageResponse.RoutineDraft from(RoutinePlan plan) {

        List<ChatMessageResponse.RoutineItem> items = new ArrayList<>();
        int seq = 1;

        for (RoutinePlan.Exercise e : plan.exercises()) {

            // 1) normalize (최소한 trim만)
            String name = e.name() == null ? "" : e.name().trim();
            if (name.isBlank()) continue;

            // 2) exact match
            var exOpt = exerciseRepositoryAdapter.findByName(name);

            // 3) fuzzy match fallback
            var ex = exOpt.orElseGet(() -> {
                List<?> candidates = exerciseRepositoryAdapter.findTop5ByNameContainingIgnoreCase(name);
                if (candidates.isEmpty()) return null;
                return (com.community.workout.domain.model.Exercise) candidates.get(0);
            });

            if (ex == null) {
                // 정책: 매핑 실패한 운동은 스킵 (v1 UX)
                continue;
            }
            items.add(new ChatMessageResponse.RoutineItem(
                    ex.getId(),
                    seq++,
                    e.sets(),
                    e.reps(),
                    e.rpe(),
                    e.restSec(),
                    null
            ));
        }

        return new ChatMessageResponse.RoutineDraft(items);
    }
}