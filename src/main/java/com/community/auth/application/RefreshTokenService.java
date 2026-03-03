package com.community.auth.application;

import com.community.auth.domain.RefreshToken;
import com.community.auth.infra.RefreshTokenRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.jwt.JWTProvider;
import com.community.global.exception.ResponseCode;
import com.community.global.jwt.TokenHashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepositoryAdapter refreshTokenRepositoryAdapter;
    private final JWTProvider jwtProvider;

    @Transactional
    public String issueAndStore(Long memberId, UUID sessionId){
        refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);

        String refreshToken = jwtProvider.issueRefreshToken(memberId);
        String tokenHash = TokenHashUtil.sha256(refreshToken);

        OffsetDateTime expiredAt = OffsetDateTime.now()
                .plusSeconds(jwtProvider.getRtExpSeconds());

        RefreshToken entity = RefreshToken.create(memberId, sessionId, tokenHash, expiredAt);
        refreshTokenRepositoryAdapter.save(entity);

        return refreshToken;
    }

    // 동시 요청이 같은 RT를 revokedAt 기준으로 구분하는 유예 시간 (ms)
    private static final long CONCURRENT_GRACE_MS = 3_000L;

    @Transactional
    public String rotate(String refreshTokenRaw) {
        Long memberId = jwtProvider.extractMemberId(refreshTokenRaw);
        String incomingHash = TokenHashUtil.sha256(refreshTokenRaw);

        // 1. SELECT FOR UPDATE — 동일 토큰 행에 대한 동시 접근 직렬화
        RefreshToken stored = refreshTokenRepositoryAdapter.findByTokenHash(incomingHash)
                .orElseThrow(() -> new CommonException(ResponseCode.INVALID_REFRESH_TOKEN));

        // 2. 만료된 토큰
        if (stored.isExpired()) {
            throw new CommonException(ResponseCode.TOKEN_EXPIRED);
        }

        // 3. 이미 revoke된 토큰: 동시 중복 vs 재사용 공격 구분
        if (stored.isRevoked()) {
            boolean isConcurrentDuplicate = stored.getRevokedAt() != null
                    && Duration.between(stored.getRevokedAt(), OffsetDateTime.now()).toMillis() < CONCURRENT_GRACE_MS;

            if (!isConcurrentDuplicate) {
                // revokedAt 이 오래됨 → 재사용 공격 — 해당 세션만 폐기
                refreshTokenRepositoryAdapter.revokeAllActiveBySessionId(stored.getSessionId());
            }
            // 동시 중복 시 세션은 보호된 채로 실패
            throw new CommonException(ResponseCode.INVALID_REFRESH_TOKEN);
        }

        // 4. 정상 rotate — sessionId 유지 (세션 연속성 보장)
        UUID sessionId = stored.getSessionId();

        // @Modifying JPQL → 즉시 UPDATE 실행 (entity 더티 마킹보다 먼저 DB 반영)
        // INSERT 전에 revoked=true 확정 → ux_refresh_token_active 부분 인덱스 위반 방지
        refreshTokenRepositoryAdapter.revokeAllActiveBySessionId(sessionId);

        String newRefreshToken = jwtProvider.issueRefreshToken(memberId);
        String newHash = TokenHashUtil.sha256(newRefreshToken);
        OffsetDateTime newExpiresAt = OffsetDateTime.now().plusSeconds(jwtProvider.getRtExpSeconds());

        refreshTokenRepositoryAdapter.save(RefreshToken.create(memberId, sessionId, newHash, newExpiresAt));

        return newRefreshToken;
    }

    @Transactional
    public void revokeAll(Long memberId) {
        refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);
    }
}
