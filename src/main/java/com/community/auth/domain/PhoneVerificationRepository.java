package com.community.auth.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public interface PhoneVerificationRepository {
    PhoneVerification save(PhoneVerification phoneVerification);

    Optional<PhoneVerification> findById(@NotBlank Long id);

    void deleteById(@NotNull Long id);

    Optional<PhoneVerification> findByPhoneNumber(@NotNull String phoneNumber);
}
