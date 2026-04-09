package com.community.auth.infra;

import com.community.auth.domain.PhoneVerification;
import com.community.auth.domain.PhoneVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PhoneVerificationRepositoryAdapter implements PhoneVerificationRepository {
    private final PhoneVerificationJpaRepository phoneVerificationJpaRepository;

    @Override
    public PhoneVerification save(PhoneVerification phoneVerification) {
        return phoneVerificationJpaRepository.save(phoneVerification);
    }
}
