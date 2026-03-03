package com.community.auth.infra;

import com.community.auth.domain.RefreshToken;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHashAndRevokedFalse(String tokenHash);

    // rotate() 진입점 — SELECT FOR UPDATE 로 동시 요청 직렬화
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    // 세션 단위 활성 토큰 조회 (재사용 공격 감지용)
    @Query("""
        select rt from RefreshToken rt
        where rt.sessionId = :sessionId
          and rt.revoked   = false
          and rt.expiresAt > CURRENT_TIMESTAMP
    """)
    Optional<RefreshToken> findActiveBySessionId(@Param("sessionId") UUID sessionId);

    List<RefreshToken> findAllByMemberIdAndRevokedFalse(Long memberId);

    @Modifying
    @Query("""
        update RefreshToken rt
        set rt.revoked = true, rt.revokedAt = CURRENT_TIMESTAMP
        where rt.memberId = :memberId
          and rt.revoked  = false
    """)
    int revokeAllActiveByMemberId(@Param("memberId") Long memberId);

    // 재사용 공격 감지 시 세션 단위 폐기
    @Modifying
    @Query("""
        update RefreshToken rt
        set rt.revoked = true, rt.revokedAt = CURRENT_TIMESTAMP
        where rt.sessionId = :sessionId
          and rt.revoked   = false
    """)
    int revokeAllActiveBySessionId(@Param("sessionId") UUID sessionId);
}
