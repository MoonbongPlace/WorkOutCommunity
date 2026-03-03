package com.community.auth.infra;

import com.community.auth.domain.RefreshToken;
import com.community.auth.domain.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHashAndRevokedFalse(tokenHash);
    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String tokenHash) {
        return refreshTokenJpaRepository.findByTokenHash(tokenHash);
    }

    @Override
    public Optional<RefreshToken> findActiveBySessionId(UUID sessionId) {
        return refreshTokenJpaRepository.findActiveBySessionId(sessionId);
    }

    @Override
    public List<RefreshToken> findAllByMemberIdAndRevokedFalse(Long memberId) {
        return refreshTokenJpaRepository.findAllByMemberIdAndRevokedFalse(memberId);
    }

    @Override
    public int revokeAllActiveByMemberId(Long memberId) {
        return refreshTokenJpaRepository.revokeAllActiveByMemberId(memberId);
    }

    @Override
    public int revokeAllActiveBySessionId(UUID sessionId) {
        return refreshTokenJpaRepository.revokeAllActiveBySessionId(sessionId);
    }

    @Override
    public void save(RefreshToken entity) {
        refreshTokenJpaRepository.save(entity);
    }
}
