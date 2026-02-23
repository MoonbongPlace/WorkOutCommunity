package com.community.chatbot.infra.persistence;

import com.community.chatbot.domain.model.AiChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AiChatMessageJpaRepository extends JpaRepository<AiChatMessage, Long> {
    @Query("""
        select m from AiChatMessage m
        where m.chat.id = :chatId
        order by m.createdAt desc
        """)
    List<AiChatMessage> findRecentMessages(@Param("chatId") Long chatId, Pageable pageable);
}
