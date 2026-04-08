package com.community.auth.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.OffsetDateTime;

@Table(name = "phone_verification")
@Entity
@Getter
public class PhoneVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "code_hash", nullable = false, unique = true)
    private String codeHash;

    @Column(name = "verified_at")
    private OffsetDateTime verifiedAt;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "expired_at")
    private OffsetDateTime expiredAt;

    @Column(name = "fail_count")
    private int failCount;
}
