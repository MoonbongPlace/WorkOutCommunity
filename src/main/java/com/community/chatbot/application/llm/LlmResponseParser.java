package com.community.chatbot.application.llm;

import com.community.chatbot.application.dto.RoutinePlan;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LlmResponseParser {

    private final ObjectMapper objectMapper;

    public Optional<RoutinePlan> parseRoutine(String rawText) {
        try {
            String json = extractJson(rawText);
            RoutinePlan plan = objectMapper.readValue(json, RoutinePlan.class);
            return Optional.of(plan);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf("{");
        int end = text.lastIndexOf("}");
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        throw new IllegalArgumentException("JSON not found");
    }

    public String stripCodeFence(String s) {
        if (s == null) return null;
        String t = s.trim();

        // ```json ... ``` 또는 ``` ... ```
        if (t.startsWith("```")) {
            t = t.replaceFirst("^```[a-zA-Z]*\\s*", ""); // 시작 ```json 제거
            t = t.replaceFirst("\\s*```\\s*$", "");      // 끝 ``` 제거
        }
        return t.trim();
    }
}