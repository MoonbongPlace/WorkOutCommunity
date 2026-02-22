package com.community.admin.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id","email","memberName","name","age","sex","role","createdAt"})
public class AdminMemberListItem {
    private Long id;
    private String email;
    private String memberName;
    private String name;
    private Integer age;
    private String sex;
    private String role;
    private OffsetDateTime createdAt;

    public static AdminMemberListItem from(Member member) {
        return new AdminMemberListItem(
                member.getId(),
                member.getEmail(),
                member.getMemberName(),
                member.getName(),
                member.getAge(),
                member.getSex(),
                member.getRole(),
                member.getCreatedAt()
        );
    }
}