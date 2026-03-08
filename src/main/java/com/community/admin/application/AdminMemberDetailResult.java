package com.community.admin.application;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","email","memberName","name","age","sex","role","status","createdAt","deletedAt","profileImage"})
public class AdminMemberDetailResult {
    private Long id;
    private String email;
    private String memberName;
    private String name;
    private Integer age;
    private String sex;
    private String role;
    private MemberStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime deletedAt;
    private String profileImage;

    public static AdminMemberDetailResult of(Member member) {
        return new AdminMemberDetailResult(
                member.getId(),
                member.getEmail(),
                member.getMemberName(),
                member.getName(),
                member.getAge(),
                member.getSex(),
                member.getRole(),
                member.getStatus(),
                member.getCreatedAt(),
                member.getDeletedAt(),
                member.getProfileImage()
        );
    }
}
