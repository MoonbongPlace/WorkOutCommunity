package com.community.chatbot.application;

import com.community.chatbot.api.ChatMessageResponse;
import com.community.chatbot.domain.model.AiChat;
import com.community.chatbot.infra.persistence.AiChatRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepositoryAdapter aiChatRepositoryAdapter;

    @Transactional
    public AiChat getOrCreateChat(Long memberId) {

        return aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));
    }

    public ChatMessageResponse processMessage(Long memberId, String message) {

        // 일단은 에코 응답 (LLM 붙이기 전 테스트용)
        return new ChatMessageResponse(
                message,
                OffsetDateTime.now()
        );
    }
}
