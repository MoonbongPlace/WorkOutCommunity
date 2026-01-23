package com.community.member.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity(name = "Member")
@Table(name="member")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String member_name;

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
    private OffsetDateTime created_at;

    @Column
    private OffsetDateTime updated_at;

    @Column
    private OffsetDateTime deleted_at;
}
