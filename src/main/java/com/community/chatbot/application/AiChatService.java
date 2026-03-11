package com.community.chatbot.application;

import com.community.chatbot.api.dto.response.ChatHistoryResponse;
import com.community.chatbot.api.dto.response.ChatMessageResponse;
import com.community.chatbot.application.llm.LlmInputFactory;
import com.community.chatbot.application.prompt.PromptFactory;
import com.community.chatbot.domain.model.AiChat;
import com.community.chatbot.domain.model.AiChatMessage;
import com.community.chatbot.domain.model.ChatRole;
import com.community.chatbot.infra.OpenAiClient;
import com.community.chatbot.infra.persistence.AiChatMessageRepositoryAdapter;
import com.community.chatbot.infra.persistence.AiChatRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepositoryAdapter aiChatRepositoryAdapter;
    private final AiChatMessageRepositoryAdapter aiChatMessageRepositoryAdapter;
    private final OpenAiClient openAiClient;

    private final PromptFactory promptFactory;
    private final LlmInputFactory llmInputFactory;

    @Transactional
    public AiChat getOrCreateChat(Long memberId) {
        return aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));
    }

    @Transactional
    public ChatMessageResponse processMessage(Long memberId, String message) {

        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));

        aiChatMessageRepositoryAdapter.save(new AiChatMessage(chat, ChatRole.USER, message));

        List<AiChatMessage> recent = aiChatMessageRepositoryAdapter.findRecentByChatId(
                chat.getId(),
                PageRequest.of(0, 20)
        );
        Collections.reverse(recent);

        String instructions = promptFactory.friendCoachInstructions();
        Object input = llmInputFactory.buildForInfoChat(recent, message);

        String assistantReply = openAiClient.generateText(instructions, input);

        if (assistantReply == null || assistantReply.isBlank()) {
            assistantReply = "음… 지금은 답이 잘 안 만들어졌어. 질문을 조금만 더 구체적으로 말해줄래?";
        }

        aiChatMessageRepositoryAdapter.save(new AiChatMessage(chat, ChatRole.ASSISTANT, assistantReply));
        chat.touch();

        return new ChatMessageResponse(assistantReply, OffsetDateTime.now());
    }

    @Transactional(readOnly = true)
    public ChatHistoryResponse getHistory(Long memberId, int limit, String order){
        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId).orElse(null);

        if (chat== null){
            return new ChatHistoryResponse(null, Collections.emptyList());
        }
        List<AiChatMessage> recent =
                aiChatMessageRepositoryAdapter.findRecentByChatId(chat.getId(), PageRequest.of(0, limit));

        if ("asc".equalsIgnoreCase(order)){
            Collections.reverse(recent);
        }

        List<ChatHistoryResponse.ChatHistoryMessage> messages = recent.stream()
                .map(m -> new ChatHistoryResponse.ChatHistoryMessage(
                        m.getId(),
                        m.getRole(),
                        m.getMessage(),
                        m.getCreatedAt()
                ))
                .toList();

        return new ChatHistoryResponse(chat.getId(), messages);
    }
}
