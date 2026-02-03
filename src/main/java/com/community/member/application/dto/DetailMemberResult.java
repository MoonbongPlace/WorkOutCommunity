package com.community.member.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "email", "memberName", "name", "age", "sex", "role", "createdAt"})
public class DetailMemberResult {
    private Long id;
    private String email;
    private String memberName;
    private String name;
    private int age;
    private String sex;
    private String role;
    private OffsetDateTime createdAt;


    public static DetailMemberResult from(Member member) {
        return new DetailMemberResult(
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
