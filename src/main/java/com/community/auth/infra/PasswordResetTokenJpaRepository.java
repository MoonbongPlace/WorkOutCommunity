package com.community.auth.infra;

import com.community.auth.domain.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenJpaRepository  extends JpaRepository<PasswordResetToken, Long> {

    void deleteAllByMemberId(Long memberId);
}
