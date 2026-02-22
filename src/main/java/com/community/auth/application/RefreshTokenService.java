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

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepositoryAdapter refreshTokenRepositoryAdapter;
    private final JWTProvider jwtProvider;

    @Transactional
    public String issueAndStore(Long memberId){
        refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);

        String refreshToken = jwtProvider.issueRefreshToken(memberId);
        String tokenHash = TokenHashUtil.sha256(refreshToken);

        LocalDateTime expiredAt = LocalDateTime.now()
                .plusSeconds(jwtProvider.getRtExpSeconds());

        RefreshToken entity = RefreshToken.create(memberId, tokenHash, expiredAt);
        refreshTokenRepositoryAdapter.save(entity);

        return refreshToken;
    }

    @Transactional
    public String rotate(String refreshTokenRaw){
        Long memberId = jwtProvider.extractMemberId(refreshTokenRaw);

        String tokenHash = TokenHashUtil.sha256(refreshTokenRaw);

        RefreshToken stored = refreshTokenRepositoryAdapter
                .findByTokenHashAndRevokedFalse(tokenHash)
                .orElseThrow(()->{
                    refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);
                    return new CommonException(ResponseCode.INVALID_REFRESH_TOKEN);
                });
        if(!stored.isActive()){
            stored.revoke();
            throw new CommonException(ResponseCode.TOKEN_EXPIRED);
        }

        stored.revoke();

        refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);

        String newRefreshToken = jwtProvider.issueRefreshToken(memberId);
        String newHash = TokenHashUtil.sha256(newRefreshToken);

        LocalDateTime newExpiresAt = LocalDateTime.now()
                .plusSeconds(jwtProvider.getRtExpSeconds());

        refreshTokenRepositoryAdapter.save(RefreshToken.create(memberId, newHash, newExpiresAt));

        return newRefreshToken;
    }

    @Transactional
    public void revokeAll(Long memberId) {
        refreshTokenRepositoryAdapter.revokeAllActiveByMemberId(memberId);
    }
}
