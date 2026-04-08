package com.community.global.component;

public interface MailSender {
    void send(String to, String subject, String htmlContent);
}
