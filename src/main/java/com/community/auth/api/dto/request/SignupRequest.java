package com.community.auth.api.dto.request;

import jakarta.validation.constraints.Email;
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
public class SignupRequest {
    @NotNull @Email
    private String email;
    @NotBlank(message = "전화번호 기입해주세요.")
    private String phoneNumber;
    @NotNull
    private String memberName;
    @NotNull
    private String password;
    @NotNull
    private String name;
    private int age;
    private String sex;
}
