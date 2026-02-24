package com.community.chatbot.api;

import com.community.chatbot.application.AiChatService;
import com.community.global.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AiChatController {
    private final AiChatService aiChatService;

    // 메세지 전송
    @PostMapping("/messages")
    public ResponseEntity<ChatMessageResponse> sendMessage(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @Valid @RequestBody ChatMessageRequest request
    ) {

        Long memberId = principal.memberId();

        ChatMessageResponse response =
                aiChatService.processMessage(memberId, request.message());

        return ResponseEntity.ok(response);
    }

    // 메세지 history 조회
    @GetMapping("/history")
    public ResponseEntity<ChatHistoryResponse> getHistory(
            @AuthenticationPrincipal CustomUserPrincipal principal,
            @RequestParam(defaultValue = "30") int limit,
            @RequestParam(defaultValue = "asc") String order
    ){
        int safeLimit = Math.min(Math.max(limit, 1), 100);

        ChatHistoryResponse response =
                aiChatService.getHistory(principal.memberId(), safeLimit, order);

        return ResponseEntity.ok(response);
    }
}
