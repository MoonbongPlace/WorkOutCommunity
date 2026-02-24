package com.community.chatbot.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ChatMessageRequest(

        @NotBlank(message = "메시지는 비어있을 수 없습니다.")
        String message

) {}