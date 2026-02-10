package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReissueRequest {
    @NotBlank
    String refreshToken;
}
