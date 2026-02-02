package com.community.auth.application;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"memberName","name","age","sex","role","createdAt"})
public class MemberSignupResult {
    private String memberName;
    private String name;
    private int age;
    private String sex;
    private String role;
    private OffsetDateTime createdAt;

    public static MemberSignupResult from(Member saved) {
        return new MemberSignupResult(
                saved.getMemberName(),
                saved.getName(),
                saved.getAge(),
                saved.getSex(),
                saved.getRole(),
                saved.getCreatedAt()
        );
    }
}
