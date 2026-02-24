package com.community.chatbot.infra.persistence;

import com.community.chatbot.domain.model.AiChatMessage;
import com.community.chatbot.domain.repository.AiChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class AiChatMessageRepositoryAdapter implements AiChatMessageRepository {
    private final AiChatMessageJpaRepository aiChatMessageJpaRepository;

    @Override
    public void save(AiChatMessage userMessage) {
        aiChatMessageJpaRepository.save(userMessage);
    }

    @Override
    public List<AiChatMessage> findRecentByChatId(Long chatId, Pageable pageable) {
            return aiChatMessageJpaRepository.findRecentByChatId(chatId, pageable);
    }


}
