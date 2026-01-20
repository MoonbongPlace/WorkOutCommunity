package com.community.board.api.dto;

public record UpdatePostRequest(
        String title,
        String body,
        String category,
        String image
) {}