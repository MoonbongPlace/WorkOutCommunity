package com.community.auth.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Ref;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
    void save(RefreshToken refreshToken);
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);
    List<RefreshToken> findAllByMemberIdAndRevokedFalse(Long memberId);
    int revokeAllActiveByMemberId(Long memberId);
}
