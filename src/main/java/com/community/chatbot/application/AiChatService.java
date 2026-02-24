package com.community.chatbot.application;

import com.community.chatbot.api.ChatHistoryResponse;
import com.community.chatbot.api.ChatMessageResponse;
import com.community.chatbot.domain.model.AiChat;
import com.community.chatbot.domain.model.AiChatMessage;
import com.community.chatbot.domain.model.ChatRole;
import com.community.chatbot.infra.OpenAiClient;
import com.community.chatbot.infra.persistence.AiChatMessageRepositoryAdapter;
import com.community.chatbot.infra.persistence.AiChatRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiChatService {
    private final AiChatRepositoryAdapter aiChatRepositoryAdapter;
    private final AiChatMessageRepositoryAdapter aiChatMessageRepositoryAdapter;
    private final OpenAiClient openAiClient;

    @Transactional
    public AiChat getOrCreateChat(Long memberId) {

        return aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));
    }

    @Transactional
    public ChatMessageResponse processMessage(Long memberId, String message) {

        // 1️⃣ 세션 조회 or 생성
        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId)
                .orElseGet(()-> aiChatRepositoryAdapter.save(new AiChat(memberId)));

        // 2️⃣ USER 메시지 저장
        AiChatMessage userMessage = new AiChatMessage(chat, ChatRole.USER, message);
        aiChatMessageRepositoryAdapter.save(userMessage);

        List<AiChatMessage> recent = aiChatMessageRepositoryAdapter.findRecentByChatId(
                chat.getId(),
                PageRequest.of(0, 20)
        );
        Collections.reverse(recent); // 과거 -> 현재

        // 3) system instructions (도메인 가드레일 포함)
        String instructions = """
                너는 운동 커뮤니티의 도메인 특화 챗봇이다.
                - 역할: 운동/영양/루틴에 대해 일반적인 코칭 정보를 제공한다.
                - 금지: 의학적 진단/처방/약물 지시는 하지 않는다. 통증/부상/질환은 전문가 상담을 권고한다.
                - 부족한 정보가 있으면 목표(감량/근비대/체력), 경력, 주당 횟수, 장비, 부상 여부를 먼저 질문한다.
                - 답변 형식: (1) 핵심 요약 (2) 구체 루틴/세트/횟수/RPE/휴식 (3) 주의사항
                """;

        // 4) inputText 구성 (v1: 텍스트로 합치기)
        String inputText = buildInputText(recent, message);

        // 5) LLM 호출
        String assistantReply = openAiClient.generateText(instructions, inputText);
        if (assistantReply.isBlank()) assistantReply = "답변 생성에 실패했어. 잠시 후 다시 시도해줘.";

        // 6) ASSISTANT 저장 + 세션 touch
        aiChatMessageRepositoryAdapter.save(new AiChatMessage(chat, ChatRole.ASSISTANT, assistantReply));
        chat.touch();

        return new ChatMessageResponse(
                assistantReply,
                OffsetDateTime.now()
        );
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

    private String buildInputText(List<AiChatMessage> recent, String latestUserMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append("아래는 최근 대화 기록이다.\n");
        for (AiChatMessage m : recent) {
            sb.append(m.getRole()).append(": ").append(m.getMessage()).append("\n");
        }
        sb.append("\n사용자 최신 질문:\n").append(latestUserMessage);
        return sb.toString();
    }
}
