package com.community.chatbot.application.llm;

import com.community.chatbot.application.intent.ChatIntent;
import com.community.chatbot.domain.model.AiChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LlmInputFactory {

    public Object build(List<AiChatMessage> recent, String latestUserMessage, ChatIntent intent) {

        StringBuilder sb = new StringBuilder();

        sb.append("[MODE]\n").append(intent.name()).append("\n\n");
        sb.append("[CHAT_HISTORY]\n");
        for (AiChatMessage m : recent) {
            sb.append(m.getRole()).append(": ").append(m.getMessage()).append("\n");
        }
        sb.append("\n[USER]\n").append(latestUserMessage);

        return List.of(
                java.util.Map.of(
                        "role", "user",
                        "content", List.of(
                                java.util.Map.of("type", "input_text", "text", sb.toString())
                        )
                )
        );
    }
}