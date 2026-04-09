package com.community.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"name", "email"})
public class FindUserIdResult {
    private String name;
    private String email;

    public static FindUserIdResult of(String name, String email) {
        return new FindUserIdResult(
                name,
                email
        );
    }
}
