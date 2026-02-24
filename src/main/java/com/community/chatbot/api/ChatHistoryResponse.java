package com.community.chatbot.api;

import com.community.chatbot.domain.model.ChatRole;

import java.time.OffsetDateTime;
import java.util.List;

public record ChatHistoryResponse(
        Long chatId,
        List<ChatHistoryMessage> messages
) {
    public record ChatHistoryMessage(
            Long id,
            ChatRole role,
            String message,
            OffsetDateTime createdAt
    ) {}
}