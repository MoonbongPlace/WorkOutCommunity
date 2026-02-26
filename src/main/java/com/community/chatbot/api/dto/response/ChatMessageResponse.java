package com.community.chatbot.api.dto.response;

import java.time.OffsetDateTime;

public record ChatMessageResponse(
        String answer,
        OffsetDateTime createdAt
) {
}