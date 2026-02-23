package com.community.chatbot.api;

import java.time.OffsetDateTime;

public record ChatMessageResponse(

        String answer,
        OffsetDateTime createdAt

) {}
