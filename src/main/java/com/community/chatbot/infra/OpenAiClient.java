package com.community.chatbot.infra;

import com.community.chatbot.infra.dto.ResponsesCreateRequest;
import com.community.chatbot.infra.dto.ResponsesCreateResponse;
import com.community.global.openai.OpenAiProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class OpenAiClient {
    private final WebClient openAiWebClient;
    private final OpenAiProperties props;

    public String generateText(String instructions, Object input) {

        ResponsesCreateRequest req = new ResponsesCreateRequest(
                props.model(),
                instructions,
                input,
                "auto",
                0.4
        );

        ResponsesCreateResponse res = openAiWebClient.post()
                .uri("/responses")
                .bodyValue(req)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        resp.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(
                                        new RuntimeException("OpenAI error: " + resp.statusCode() + " body=" + body)
                                ))
                )
                .bodyToMono(ResponsesCreateResponse.class)
                .block();

        return extractOutputText(res);
    }

    private String extractOutputText(ResponsesCreateResponse res) {
        if (res == null || res.output() == null) return "";

        StringBuilder sb = new StringBuilder();

        for (var item : res.output()) {
            if (item == null || item.content() == null) continue;

            for (var c : item.content()) {
                if (c == null) continue;

                if ("output_text".equals(c.type()) && c.text() != null) {
                    sb.append(c.text());
                }
            }
        }
        return sb.toString().trim();
    }
}
