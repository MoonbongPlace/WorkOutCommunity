package com.community.auth.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name="password_reset_token")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="member_id", nullable = false, unique = true)
    private Long memberId;

    @Column(name="token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name="expired_at")
    private OffsetDateTime expiredAt;

    @Column(name="used_at")
    private OffsetDateTime usedAt;

    @Column(name="used", nullable = false)
    private boolean used;

    @Column(name="created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static PasswordResetToken create(Long memberId, String tokenHash, OffsetDateTime expiredAt) {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.memberId = memberId;
        passwordResetToken.tokenHash = tokenHash;
        passwordResetToken.expiredAt = expiredAt;
        passwordResetToken.usedAt = null;
        passwordResetToken.used = false;
        passwordResetToken.createdAt = OffsetDateTime.now();
        return passwordResetToken;
    }
}
