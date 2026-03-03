package com.community.auth.domain;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);
    Optional<RefreshToken> findByTokenHash(String tokenHash);
    Optional<RefreshToken> findActiveBySessionId(UUID sessionId);
    List<RefreshToken> findAllByMemberIdAndRevokedFalse(Long memberId);
    int revokeAllActiveByMemberId(Long memberId);
    int revokeAllActiveBySessionId(UUID sessionId);
}
