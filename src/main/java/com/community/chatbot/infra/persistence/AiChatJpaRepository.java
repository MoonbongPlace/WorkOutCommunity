package com.community.chatbot.infra.persistence;

import com.community.chatbot.domain.model.AiChat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AiChatJpaRepository extends JpaRepository<AiChat, Long> {
    Optional<AiChat> findByMemberId(Long memberId);
}
