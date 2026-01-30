package com.community.member.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"id","memberName", "role"})
public class DeletedMemberResult {

    private Long id;
    private String memberName;
    private String role;
    public static DeletedMemberResult from(Member saved) {
        return new DeletedMemberResult(
                saved.getId(),
                saved.getMemberName(),
                saved.getRole()
        );
    }
}
