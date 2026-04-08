package com.community.auth.application;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "email","codeHash","createdAt"})
public class EmailVerifyResult {
    private Long id;
    private String email;
    private String codeHash;
    private OffsetDateTime createdAt;
}
