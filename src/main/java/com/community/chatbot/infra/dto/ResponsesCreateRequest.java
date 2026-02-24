package com.community.chatbot.infra.dto;

public record ResponsesCreateRequest(
        String model,
        String instructions,
        Object input,
        String truncation,
        Double temperature
) {}

