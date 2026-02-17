package com.community.notification.api.dto.request;

import com.community.notification.domain.model.NotificationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationCreateRequest {
    @NotNull
    Long postId;
    @NotNull
    NotificationType type;
    @NotBlank
    String message;
}
