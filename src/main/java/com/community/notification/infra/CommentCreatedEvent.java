package com.community.notification.infra;

public record CommentCreatedEvent(
        Long postId,
        Long commenterId,
        String message
) {}