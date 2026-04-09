package com.community.member.domain.model;

import com.community.member.api.dto.request.UpdateMemberRequest;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "profile_image", length = 512)
    private String profileImage;

    @Column
    private Integer age;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;

    public static Member signup(
            @NotNull
            String email,
            @NotBlank
            String phoneNumber,
            @NotNull
            String memberName,
            String encodedPassword,
            @NotNull
            String name,
            Integer age,
            String sex,
            String role,
            OffsetDateTime createdAt,
            MemberStatus status,
            String profileImageUrl
    ) {
        Member member = new Member();
        member.email = email;
        member.phoneNumber = phoneNumber;
        member.memberName = memberName;
        member.password = encodedPassword;
        member.name = name;
        member.age = age;
        member.sex = sex;
        member.role = role;
        member.createdAt = createdAt;
        member.status = status;
        member.profileImage = profileImageUrl;

        return member;
    }

    public void updateMember(UpdateMemberRequest request, String profileImageUrl) {
        if (request.getMemberName() != null && !request.getMemberName().isBlank()) {
            this.memberName = request.getMemberName();
        }
        if (request.getName() != null && !request.getName().isBlank()) {
            this.name = request.getName();
        }
        if (request.getAge() != null) {
            this.age = request.getAge();
        }
        if (request.getSex() != null && !request.getSex().isBlank()) {
            this.sex = request.getSex();
        }
        if (profileImageUrl != null && !profileImageUrl.isBlank()) {
            this.profileImage = profileImageUrl;
        }
        this.updatedAt = OffsetDateTime.now();
    }

    public void changeStatus(MemberStatus status) {
        this.status = status;
        this.updatedAt = OffsetDateTime.now();
    }

    public void deleteMember() {
        this.deletedAt = OffsetDateTime.now();
        this.status = MemberStatus.DELETED;
    }
}
