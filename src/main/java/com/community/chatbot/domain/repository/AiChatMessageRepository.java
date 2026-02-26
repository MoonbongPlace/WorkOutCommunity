package com.community.chatbot.domain.repository;

import com.community.chatbot.domain.model.AiChatMessage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AiChatMessageRepository {
    void save(AiChatMessage userMessage);

    List<AiChatMessage>  findRecentByChatId(Long chatId, Pageable pageable);
}
