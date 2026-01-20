package com.community.board.api.dto;

import java.time.OffsetDateTime;

public record PostResponse(
        Long id,
        String title,
        String body,
        String category,
        Long views,
        Long authorId,
        String image,
        OffsetDateTime createdAt
) {}