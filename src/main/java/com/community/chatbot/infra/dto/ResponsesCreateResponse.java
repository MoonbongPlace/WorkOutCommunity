package com.community.chatbot.infra.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ResponsesCreateResponse(
        List<OutputItem> output
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record OutputItem(
            String type,
            List<ContentItem> content
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContentItem(
            String type,
            String text
    ) {}
}