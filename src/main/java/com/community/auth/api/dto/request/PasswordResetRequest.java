package com.community.auth.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
@AllArgsConstructor
public class PasswordResetRequest {
    @NotNull
    private String name;
    @NotNull
    private String email;
}
