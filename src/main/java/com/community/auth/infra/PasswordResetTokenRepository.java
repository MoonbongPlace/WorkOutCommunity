package com.community.auth.infra;

import com.community.auth.domain.PasswordResetToken;

public interface PasswordResetTokenRepository {
    void save(PasswordResetToken passwordResetToken);

    void deleteAllByMemberId(Long id);
}
