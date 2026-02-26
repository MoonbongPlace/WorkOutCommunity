//package com.community.chatbot.application;
//
//import com.community.chatbot.api.dto.response.ChatHistoryResponse;
//import com.community.chatbot.api.dto.response.ChatMessageResponse;
//import com.community.chatbot.application.dto.RoutinePlan;
//import com.community.chatbot.application.intent.ChatIntent;
//import com.community.chatbot.application.intent.ChatIntentClassifier;
//import com.community.chatbot.application.llm.LlmInputFactory;
//import com.community.chatbot.application.llm.LlmResponseParser;
//import com.community.chatbot.application.prompt.PromptFactory;
//import com.community.chatbot.domain.model.AiChat;
//import com.community.chatbot.domain.model.AiChatMessage;
//import com.community.chatbot.domain.model.ChatRole;
//import com.community.chatbot.infra.OpenAiClient;
//import com.community.chatbot.infra.persistence.AiChatMessageRepositoryAdapter;
//import com.community.chatbot.infra.persistence.AiChatRepositoryAdapter;
//import com.community.workout.infra.RoutineDraftFactory;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.OffsetDateTime;
//import java.util.Collections;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class AiChatService {
//    private final AiChatRepositoryAdapter aiChatRepositoryAdapter;
//    private final AiChatMessageRepositoryAdapter aiChatMessageRepositoryAdapter;
//    private final OpenAiClient openAiClient;
//    private final RoutineDraftFactory routineDraftFactory;
//
//    private final ChatIntentClassifier intentClassifier;
//    private final PromptFactory promptFactory;
//    private final LlmInputFactory llmInputFactory;
//    private final LlmResponseParser llmResponseParser;
//
//    @Transactional
//    public AiChat getOrCreateChat(Long memberId) {
//        return aiChatRepositoryAdapter.findByMemberId(memberId)
//                .orElseGet(() -> aiChatRepositoryAdapter.save(new AiChat(memberId)));
//    }
//
//    @Transactional
//    public ChatMessageResponse processMessage(Long memberId, String message) {
//
//        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId)
//                .orElseGet(()-> aiChatRepositoryAdapter.save(new AiChat(memberId)));
//
//        AiChatMessage userMessage = new AiChatMessage(chat, ChatRole.USER, message);
//        aiChatMessageRepositoryAdapter.save(userMessage);
//
//        List<AiChatMessage> recent = aiChatMessageRepositoryAdapter.findRecentByChatId(
//                chat.getId(),
//                PageRequest.of(0, 20)
//        );
//        Collections.reverse(recent);
//
//        ChatIntent intent = intentClassifier.classify(message);
//        String instructions = promptFactory.instructions(intent);
//        Object input = llmInputFactory.build(recent, message, intent);
//
//        String assistantReply = openAiClient.generateText(instructions, input);
//
//        if (assistantReply == null || assistantReply.isBlank()) {
//            assistantReply = "답변 생성에 실패했어. 잠시 후 다시 시도해줘.";
//        }
//
//        ChatMessageResponse.ChatMode mode = (intent == ChatIntent.ROUTINE)
//                ? ChatMessageResponse.ChatMode.ROUTINE
//                : ChatMessageResponse.ChatMode.COACH;
//
//        ChatMessageResponse.RoutineDraft draft = null;
//
//        // ROUTINE 분기: 파싱 성공 시에만 draft 생성
//        if (intent == ChatIntent.ROUTINE) {
//
//            log.debug("[ROUTINE] raw assistantReply={}", assistantReply);
//
//            String normalized = llmResponseParser.stripCodeFence(assistantReply);
//            log.debug("[ROUTINE] normalized={}", normalized);
//
//            Optional<RoutinePlan> planOpt = llmResponseParser.parseRoutine(normalized);
//            log.debug("[ROUTINE] parsed present? {}", planOpt.isPresent());
//
//            if (planOpt.isEmpty()) {
//                // 1) JSON 파싱 자체가 실패: 사용자에게 재질문(코치 텍스트)
//                assistantReply = """
//        루틴을 만들기 위한 정보가 부족하거나 형식이 맞지 않았어.
//        아래를 알려주면 오늘 루틴을 JSON으로 만들어줄게:
//        1) 목표(감량/근비대/체력)
//        2) 주당 가능 횟수/1회 운동 시간
//        3) 장비(헬스장/홈트/덤벨)
//        4) 운동 경력(초보/중급/고급)
//        5) 부상 여부
//        최근 2회 루틴에서 사용한 운동명은 가능한 한 재사용하지 말 것
//        JSON형식으로만 출력하고, 백틱 코드블록(```) 금지
//        """;
//                draft = null; // 저장 버튼 X
//            } else {
//                RoutinePlan plan = planOpt.get();
//
//                // 2) 파싱은 성공했지만, 저장용 draft 변환(=exerciseId 매핑)이 실패할 수 있음
//                Optional<ChatMessageResponse.RoutineDraft> draftOpt = routineDraftFactory.from(plan);
//
//                if (draftOpt.isEmpty()) {
//                    draft = null; // 저장 버튼 X
//
//                    // 사용자에게 "저장 불가 사유"를 알려줌 (LLM 원문 JSON을 그대로 노출하지 않도록 텍스트로)
//                    assistantReply = """
//            루틴은 만들었는데, 일부 운동명을 DB에서 찾지 못해서 저장용 초안(draft)을 만들지 못했어.
//            운동명을 조금 더 정확히 입력해줘. 예)
//            - 바벨 로우 → 바벨로우 / 바벨로우(Barbell Row)
//            - 랫 풀다운 → 랫풀다운 / 랫풀다운(Lat Pulldown)
//
//            원하면 “헬스장 등 운동 루틴을 바벨/덤벨/머신 기준으로 추천해줘”처럼 장비까지 함께 말해줘.
//            """;
//
//                    log.debug("[ROUTINE] draft creation failed (exercise mapping issue). planSummary={}", plan.summary());
//                } else {
//                    draft = draftOpt.get();
//
//                    log.debug("[ROUTINE] draft is null? false");
//                    log.debug("[ROUTINE] draft items size={}", (draft.items() == null) ? null : draft.items().size());
//
//                    // (선택) UX: assistantReply를 사람이 읽는 요약으로 바꾸고 싶으면 여기서 교체
//                    // assistantReply = "오늘 루틴을 추천했어. 아래 ‘저장’ 버튼으로 운동일지에 추가할 수 있어.";
//                }
//            }
//        }
//
//        aiChatMessageRepositoryAdapter.save(new AiChatMessage(chat, ChatRole.ASSISTANT, assistantReply));
//        chat.touch();
//
//        OffsetDateTime now = OffsetDateTime.now();
//        return new ChatMessageResponse(assistantReply, now, mode, draft);
//    }
//
//    @Transactional(readOnly = true)
//    public ChatHistoryResponse getHistory(Long memberId, int limit, String order){
//        AiChat chat = aiChatRepositoryAdapter.findByMemberId(memberId).orElse(null);
//
//        if (chat== null){
//            return new ChatHistoryResponse(null, Collections.emptyList());
//        }
//        List<AiChatMessage> recent =
//                aiChatMessageRepositoryAdapter.findRecentByChatId(chat.getId(), PageRequest.of(0, limit));
//
//        if ("asc".equalsIgnoreCase(order)){
//            Collections.reverse(recent);
//        }
//
//        List<ChatHistoryResponse.ChatHistoryMessage> messages = recent.stream()
//                .map(m -> new ChatHistoryResponse.ChatHistoryMessage(
//                        m.getId(),
//                        m.getRole(),
//                        m.getMessage(),
//                        m.getCreatedAt()
//                ))
//                .toList();
//
//        return new ChatHistoryResponse(chat.getId(), messages);
//    }
//
//    private String buildInputText( List<AiChatMessage> recent, String latestUserMessage) {
//
//        StringBuilder sb = new StringBuilder();
//        sb.append("[CHAT_HISTORY]\n");
//        for (AiChatMessage m : recent) {
//            sb.append(m.getRole()).append(": ").append(m.getMessage()).append("\n");
//        }
//
//        sb.append("\n[USER]\n").append(latestUserMessage);
//        return sb.toString();
//    }
//}
