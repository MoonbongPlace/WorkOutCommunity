package com.community.chatbot.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "ai_chat_messages",
        indexes = {
                @Index(name = "idx_ai_chat_messages_chat_id_created_at",
                        columnList = "chat_id, created_at DESC")
        })
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AiChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private AiChat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private ChatRole role;

    @Column(name = "message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public AiChatMessage(AiChat chat, ChatRole role, String message) {
        this.chat = chat;
        this.role = role;
        this.message = message;
        this.createdAt = OffsetDateTime.now();
    }
}
