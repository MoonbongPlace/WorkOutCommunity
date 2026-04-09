package com.community.auth.infra;


import com.community.auth.domain.PhoneVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerification, Long> {
}
