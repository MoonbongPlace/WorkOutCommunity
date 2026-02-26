package com.community.chatbot.infra.persistence;

import com.community.chatbot.domain.model.AiChat;
import com.community.chatbot.domain.repository.AiChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AiChatRepositoryAdapter implements AiChatRepository {
    private final AiChatJpaRepository aiChatJpaRepository;

    @Override
    public Optional<AiChat> findByMemberId(Long memberId) {
        return aiChatJpaRepository.findByMemberId(memberId);
    }

    @Override
    public AiChat save(AiChat aiChat) {
        return aiChatJpaRepository.save(aiChat);
    }
}
