package com.community.auth.infra;

import com.community.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    List<RefreshToken> findAllByMemberIdAndRevokedFalse(Long memberId);

    @Modifying
    @Query("""
        update RefreshToken rt
        set rt.revoked = true, rt.revokedAt = CURRENT_TIMESTAMP
        where rt.memberId = :memberId
          and rt.revoked = false
    """)
    int revokeAllActiveByMemberId(@Param("memberId") Long memberId);
}
