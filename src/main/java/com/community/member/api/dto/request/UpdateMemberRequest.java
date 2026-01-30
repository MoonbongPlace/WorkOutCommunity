package com.community.member.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMemberRequest {
    private String memberName;
    private String password;
    private String name;
    private int age;
    private String sex;

}
