package com.community.chatbot.application.intent;

import org.springframework.stereotype.Component;

@Component
public class ChatIntentClassifier {

    public ChatIntent classify(String message) {
        String m = message.toLowerCase();

        boolean routine = m.contains("루틴")
                || m.contains("운동 추천")
                || m.contains("오늘 운동")
                || m.contains("분할")
                || m.contains("상체")
                || m.contains("하체");

        return routine ? ChatIntent.ROUTINE : ChatIntent.GENERAL;
    }
}