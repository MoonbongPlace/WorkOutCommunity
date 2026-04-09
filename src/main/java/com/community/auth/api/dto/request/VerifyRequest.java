package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyRequest {
    @NotNull
    private String phoneNumber;
}
