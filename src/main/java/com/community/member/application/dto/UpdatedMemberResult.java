package com.community.member.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"id", "memberName", "password", "name", "age", "sex", "role", "updatedAt"})
public class UpdatedMemberResult {
    private Long id;
    private String memberName;
    private String password;
    private String name;
    private Integer age;
    private String sex;
    private String role;
    private OffsetDateTime updatedAt;

    public static UpdatedMemberResult from(Member saved) {
        return new UpdatedMemberResult(
                saved.getId(),
                saved.getMemberName(),
                saved.getPassword(),
                saved.getName(),
                saved.getAge(),
                saved.getSex(),
                saved.getRole(),
                saved.getUpdatedAt()
        );
    }
}
