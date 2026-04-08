package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailRequest {
    @NotBlank(message = "이메일은 기입해주세요.")
    private String email;
}
