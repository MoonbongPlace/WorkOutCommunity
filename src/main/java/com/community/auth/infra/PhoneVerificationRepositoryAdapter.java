package com.community.auth.infra;

import com.community.auth.domain.PhoneVerification;
import com.community.auth.domain.PhoneVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PhoneVerificationRepositoryAdapter implements PhoneVerificationRepository {
    private final PhoneVerificationJpaRepository phoneVerificationJpaRepository;

    @Override
    public PhoneVerification save(PhoneVerification phoneVerification) {
        return phoneVerificationJpaRepository.save(phoneVerification);
    }

    @Override
    public Optional<PhoneVerification> findById(Long id) {
        return phoneVerificationJpaRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        phoneVerificationJpaRepository.deleteById(id);
    }

    @Override
    public Optional<PhoneVerification> findByPhoneNumber(String phoneNumber) {
        return phoneVerificationJpaRepository.findByPhoneNumber(phoneNumber);
    }
}
