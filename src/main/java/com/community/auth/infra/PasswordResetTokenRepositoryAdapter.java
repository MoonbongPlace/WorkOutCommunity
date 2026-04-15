package com.community.auth.infra;

import com.community.auth.domain.PasswordResetToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository{
    private final PasswordResetTokenJpaRepository passwordResetTokenJpaRepository;

    @Override
    public void save(PasswordResetToken passwordResetToken) {
        passwordResetTokenJpaRepository.save(passwordResetToken);
    }

    @Override
    public void deleteAllByMemberId(Long memberId) {
        passwordResetTokenJpaRepository.deleteAllByMemberId(memberId);
    }
}
