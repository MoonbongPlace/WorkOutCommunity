package com.community.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jnr.a64asm.Offset;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "phone_verification")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public static PhoneVerification create(@NotNull String phoneNumber, String verificationNumber) {
        PhoneVerification phoneVerification = new PhoneVerification();
        phoneVerification.setPhoneNumber(phoneNumber);
        phoneVerification.setCodeHash(verificationNumber);
        phoneVerification.setCreatedAt(OffsetDateTime.now());
        phoneVerification.setExpiredAt(OffsetDateTime.now().plusMinutes(3));

        return phoneVerification;
    }
}
