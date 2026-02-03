package com.community.auth.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
    @NotNull
    private String email;
    @NotNull
    private String memberName;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private int age;
    private String sex;
    private String role;
}
