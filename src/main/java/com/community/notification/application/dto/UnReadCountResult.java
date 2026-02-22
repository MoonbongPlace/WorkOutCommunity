package com.community.notification.application.dto;

public record UnReadCountResult(long unreadCount) {
    public static UnReadCountResult of(long unreadCount) {
        return new UnReadCountResult(unreadCount);
    }
}
