package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VerifyResultRequest {
    @NotNull
    private Long id;
    @NotBlank
    private String verificationCode;
}
