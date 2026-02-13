package com.community.auth.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"accessToken", "refreshToken", "email", "memberName", "role"})
public class MemberSigninResult {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String memberName;
    private String role;

    public static MemberSigninResult from(String accessToken, String refreshToken, Member member) {
        return new MemberSigninResult(
                accessToken,
                refreshToken,
                member.getEmail(),
                member.getMemberName(),
                member.getRole()
        );
    }
}
