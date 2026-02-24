package com.community.chatbot.application;

import com.community.chatbot.api.dto.response.ChatHistoryResponse;
import com.community.chatbot.api.dto.response.ChatMessageResponse;
import com.community.chatbot.application.dto.RoutinePlan;
import com.community.chatbot.application.intent.ChatIntent;
import com.community.chatbot.application.intent.ChatIntentClassifier;
import com.community.chatbot.application.llm.LlmInputFactory;
import com.community.chatbot.application.llm.LlmResponseParser;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepositoryAdapter aiChatRepositoryAdapter;
    private final AiChatMessageRepositoryAdapter aiChatMessageRepositoryAdapter;
    private final OpenAiClient openAiClient;

    private final ChatIntentClassifier intentClassifier;
    private final PromptFactory promptFactory;
    private final LlmInputFactory llmInputFactory;
    private final LlmResponseParser llmResponseParser;

    @Transactional
    public AiChat getOrCreateChat(Long memberId) {
        return aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));
    }

    @Transactional
    public ChatMessageResponse processMessage(Long memberId, String message) {

        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(()-> aiChatRepositoryAdapter.save(new AiChat(memberId)));

        AiChatMessage userMessage = new AiChatMessage(chat, ChatRole.USER, message);
        aiChatMessageRepositoryAdapter.save(userMessage);

        List<AiChatMessage> recent = aiChatMessageRepositoryAdapter.findRecentByChatId(
                chat.getId(),
                PageRequest.of(0, 20)
        );
        Collections.reverse(recent);

        ChatIntent intent = intentClassifier.classify(message);
        String instructions = promptFactory.instructions(intent);
        Object input = llmInputFactory.build(recent, message, intent);

        String assistantReply = openAiClient.generateText(instructions, input);

        if (assistantReply == null || assistantReply.isBlank()) {
            assistantReply = "답변 생성에 실패했어. 잠시 후 다시 시도해줘.";
        }

        ChatMessageResponse.ChatMode mode = (intent == ChatIntent.ROUTINE)
                ? ChatMessageResponse.ChatMode.ROUTINE
                : ChatMessageResponse.ChatMode.COACH;

        ChatMessageResponse.RoutineDraft draft = null;

        // ROUTINE 분기: 파싱 성공 시에만 draft 생성
        if (intent == ChatIntent.ROUTINE) {
            Optional<RoutinePlan> parsed = llmResponseParser.parseRoutine(assistantReply);

            if (parsed.isEmpty()) {
                assistantReply = """
                루틴을 만들기 위한 정보가 부족하거나 형식이 맞지 않았어.
                아래를 알려주면 오늘 루틴을 JSON으로 만들어줄게:
                1) 목표(감량/근비대/체력)
                2) 주당 가능 횟수/1회 운동 시간
                3) 장비(헬스장/홈트/덤벨)
                4) 운동 경력(초보/중급/고급)
                5) 부상 여부
                """;
                // draft는 null 유지 -> 프론트에서 저장 버튼 안 뜸
            } else {
                // RoutinePlan -> RoutineDraft(= 저장용 payload) 변환
                draft = routineDraftFactory.from(parsed.get()); // 아래에서 구현 예시 제공
            }
        }

        aiChatMessageRepositoryAdapter.save(new AiChatMessage(chat, ChatRole.ASSISTANT, assistantReply));
        chat.touch();

        return new ChatMessageResponse(assistantReply, OffsetDateTime.now(), ChatMessageResponse.ChatMode.COACH, null);
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

    private String buildInputText( List<AiChatMessage> recent, String latestUserMessage) {

        StringBuilder sb = new StringBuilder();
        sb.append("[CHAT_HISTORY]\n");
        for (AiChatMessage m : recent) {
            sb.append(m.getRole()).append(": ").append(m.getMessage()).append("\n");
        }

        sb.append("\n[USER]\n").append(latestUserMessage);
        return sb.toString();
    }
}
