package com.community.notification.application;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "postId", "isRead", "readAt"})
public class ReadOneResult {
    private Long id;
    private Long postId;
    private boolean isRead;
    private OffsetDateTime readAt;

    public static ReadOneResult of(Long id, Long postId, boolean isRead, OffsetDateTime readAt) {
        return new ReadOneResult(
                id,
                postId,
                isRead,
                readAt
        );
    }
}
