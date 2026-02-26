package com.community.chatbot.application.llm;

import com.community.chatbot.application.intent.ChatIntent;
import com.community.chatbot.domain.model.AiChatMessage;
import com.community.chatbot.domain.model.ChatRole;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class LlmInputFactory {

    public Object buildForInfoChat(List<AiChatMessage> recent, String currentMessage) {

        List<Map<String, String>> messages = new ArrayList<>();

        // 1️⃣ 최근 대화 맥락
        for (AiChatMessage msg : recent) {
            if (msg.getRole() == ChatRole.USER) {
                messages.add(Map.of(
                        "role", "user",
                        "content", msg.getMessage()
                ));
            } else if (msg.getRole() == ChatRole.ASSISTANT) {
                messages.add(Map.of(
                        "role", "assistant",
                        "content", msg.getMessage()
                ));
            }
        }

        // 2️⃣ 현재 사용자 메시지 추가 (마지막에 반드시 위치)
        messages.add(Map.of(
                "role", "user",
                "content", currentMessage
        ));

        return messages;
    }
}