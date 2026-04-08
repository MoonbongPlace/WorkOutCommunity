package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class VerifyRequest {
    @NotNull
    private String phoneNumber;
}
