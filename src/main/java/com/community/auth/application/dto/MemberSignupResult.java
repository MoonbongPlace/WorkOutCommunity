package com.community.auth.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"memberName","name","role","createdAt"})
public class MemberSignupResult {
    private String memberName;
    private String name;
    private String role;
    private OffsetDateTime createdAt;

    public static MemberSignupResult from(Member saved) {
        return new MemberSignupResult(
                saved.getMemberName(),
                saved.getName(),
                saved.getRole(),
                saved.getCreatedAt()
        );
    }
}
