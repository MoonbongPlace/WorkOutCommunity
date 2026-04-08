package com.community.global.component;

import com.community.global.component.property.SesProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sesv2.SesV2Client;
import software.amazon.awssdk.services.sesv2.model.*;

@Component
@RequiredArgsConstructor
public class SesMailSender implements MailSender{
    private final SesV2Client sesV2Client;
    private final SesProperties sesProperties;

    @Override
    public void send(String to, String subject, String htmlContent) {
        SendEmailRequest request = SendEmailRequest.builder()
                .fromEmailAddress(sesProperties.getFromAddress())
                .destination(Destination.builder()
                        .toAddresses(to)
                        .build())
                .content(EmailContent.builder()
                        .simple(Message.builder()
                                .subject(Content.builder()
                                .data(subject)
                                .charset("UTF-8")
                                .build())
                        .body(Body.builder()
                                .html(Content.builder()
                                        .data(htmlContent)
                                        .charset("UTF-8")
                                        .build())
                                .build())
                        .build())
                .build())
                .build();

        sesV2Client.sendEmail(request);
    }
}
