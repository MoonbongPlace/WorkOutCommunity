package com.community.workout.infra;

import com.community.chatbot.api.dto.response.ChatMessageResponse;
import com.community.chatbot.application.dto.RoutinePlan;
import com.community.workout.domain.model.Exercise;
import com.community.workout.infra.persistence.adapter.ExerciseRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoutineDraftFactory {

    private final ExerciseRepositoryAdapter exerciseRepositoryAdapter;

    public Optional<ChatMessageResponse.RoutineDraft> from(RoutinePlan plan) {

        List<ChatMessageResponse.RoutineItem> items = new ArrayList<>();
        List<String> failed = new ArrayList<>();
        int seq = 1;

        for (RoutinePlan.Exercise e : plan.exercises()) {
            String raw = e.name();
            String name = raw == null ? "" : raw.trim();
            if (name.isBlank()) continue;

            Exercise ex = resolveExercise(name);

            if (ex == null) {
                failed.add(name);               // ✅ 누락 기록
                continue;                       // v1 정책: 매핑 실패 운동은 스킵
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

        if (!failed.isEmpty()) {
            log.debug("[ROUTINE] unmapped exercises={}", failed);
        }

        if (items.isEmpty()) {
            return Optional.empty();   // 저장 버튼 안 뜨게
        }

        return Optional.of(new ChatMessageResponse.RoutineDraft(items));
    }

    private Exercise resolveExercise(String inputName) {
        Optional<Exercise> exact = exerciseRepositoryAdapter.findByName(inputName);
        if (exact.isPresent()) return exact.get();

        List<Exercise> candidates =
                exerciseRepositoryAdapter.findTop5ByNameContainingIgnoreCase(inputName);

        if (candidates.isEmpty()) return null;

        return candidates.get(0);
    }

    private int score(String a, String b) {
        if (a.isBlank() || b.isBlank()) return 0;
        if (a.equals(b)) return 1_000;

        int sc = 0;
        if (a.contains(b) || b.contains(a)) sc += 300;

        // 핵심 키워드 가산(원하면 확장)
        String[] tokens = {"로우", "풀다운", "풀업", "프레스", "컬", "익스텐션", "스쿼트", "데드", "플라이", "레그"};
        for (String t : tokens) {
            if (a.contains(t) && b.contains(t)) sc += 50;
        }
        return sc;
    }

    private String norm(String s) {
        if (s == null) return "";
        return s.toLowerCase()
                .replaceAll("\\s+", "")          // 공백 제거
                .replaceAll("[^0-9a-z가-힣]", ""); // 특수문자 제거
    }
}