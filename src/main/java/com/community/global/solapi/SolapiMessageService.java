package com.community.global.solapi;

import com.solapi.sdk.NurigoApp;
import com.solapi.sdk.message.dto.request.SingleMessageSendingRequest;
import com.solapi.sdk.message.model.Message;
import com.solapi.sdk.message.service.DefaultMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SolapiMessageService {
    private final DefaultMessageService messageService;

    @Value("${solapi.api-phone-number}")
    private String phoneNumber;

    public SolapiMessageService(@Value("${solapi.api-key}") String apiKey,
                          @Value("${solapi.api-secret}") String apiSecret) {
        // SDK 초기화
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.solapi.com");
    }

    public void sendVerificationSms(String to, String code) {
        Message message = new Message();
        message.setFrom(phoneNumber); // 하이픈 제외 숫자만
        message.setTo(to);
        message.setText("인증번호는 " + code + " 입니다. 3분 내로 입력해 주세요.");

        try {
            this.messageService.send(message);
        } catch (Exception e) {
            log.info(e.toString());
            // 발송 실패 시 예외 처리 (로그 기록 등)
            throw new RuntimeException("SMS 발송에 실패했습니다.");
        }
    }


}
