package com.community.member.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity(name = "Member")
@Table(name="member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private int age;

    @Column
    private String sex;

    @Column(nullable = false)
    private String role = "user";

    @Column(nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column
    private OffsetDateTime updatedAt;

    @Column
    private OffsetDateTime deletedAt;

    public static Member signup(
            @NotNull
            String email,
            @NotNull
            String memberName,
            String encodedPassword,
            @NotNull
            String name,
            int age,
            String sex,
            String role,
            OffsetDateTime createdAt) {
        Member member = new Member();
        member.email = email;
        member.memberName = memberName;
        member.password = encodedPassword;
        member.name = name;
        member.age = age;
        member.sex = sex;
        member.role = role;
        member.createdAt = createdAt;

        return member;
    }
}
