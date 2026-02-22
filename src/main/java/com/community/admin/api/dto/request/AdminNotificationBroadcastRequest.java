package com.community.admin.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminNotificationBroadcastRequest {
    @NotBlank
    String message;
    String linkUrl;
}
