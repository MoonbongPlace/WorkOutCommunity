package com.community.auth.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "refresh_token")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, updatable = false)
    private Long memberId;

    @Column(name = "token_hash", nullable = false, unique = true)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(nullable = false)
    private boolean revoked = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "revoked_at")
    private OffsetDateTime revokedAt;

    @Column(name = "session_id", nullable = false, updatable = false)
    private UUID sessionId;

    public static RefreshToken create(
            Long memberId,
            UUID sessionId,
            String tokenHash,
            OffsetDateTime expiresAt
    ) {
        RefreshToken token = new RefreshToken();
        token.memberId = memberId;
        token.sessionId = sessionId;
        token.tokenHash = tokenHash;
        token.expiresAt = expiresAt;
        token.createdAt = OffsetDateTime.now();
        return token;
    }

    public void revoke() {
        this.revoked = true;
        this.revokedAt = OffsetDateTime.now();
    }

    public boolean isExpired() {
        return OffsetDateTime.now().isAfter(this.expiresAt);
    }

    public boolean isActive() {
        return !revoked && !isExpired();
    }

}
