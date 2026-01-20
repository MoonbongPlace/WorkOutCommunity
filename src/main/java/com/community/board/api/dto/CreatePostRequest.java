package com.community.board.api.dto;

public record CreatePostRequest(
        @NotBlank String title,
        @NotBlank String body,
        @NotBlank String category,
        String image
) {}