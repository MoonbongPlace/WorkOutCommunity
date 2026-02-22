package com.community.member.application.dto;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "email", "memberName", "name", "age", "sex", "role", "createdAt", "status"})
public class DetailMemberResult {
    private Long id;
    private String email;
    private String memberName;
    private String name;
    private Integer age;
    private String sex;
    private String role;
    private OffsetDateTime createdAt;
    private MemberStatus status;


    public static DetailMemberResult from(Member member) {
        return new DetailMemberResult(
                member.getId(),
                member.getEmail(),
                member.getMemberName(),
                member.getName(),
                member.getAge(),
                member.getSex(),
                member.getRole(),
                member.getCreatedAt(),
                member.getStatus()
        );
    }
}
