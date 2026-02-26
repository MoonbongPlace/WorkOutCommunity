package com.community.chatbot.domain.repository;

import com.community.chatbot.domain.model.AiChat;

import java.util.Optional;

public interface AiChatRepository {
    Optional<AiChat> findByMemberId(Long memberId);

    AiChat save(AiChat aiChat);
}
