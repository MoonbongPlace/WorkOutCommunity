package com.community.chatbot.api;

import com.community.chatbot.application.AiChatService;
import com.community.global.CustomUserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AiChatController {
    private final AiChatService aiChatService;

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
}
