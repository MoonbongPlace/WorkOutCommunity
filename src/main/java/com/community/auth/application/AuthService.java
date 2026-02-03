package com.community.auth.application;

import com.community.auth.api.dto.request.SignupRequest;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    @Transactional
    public MemberSignupResult signup(@Valid SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = Member.signup(
                request.getEmail(),
                request.getMemberName(),
                encodedPassword,
                request.getName(),
                request.getAge(),
                request.getSex(),
                request.getRole(),
                OffsetDateTime.now()
        );

        Member saved = memberRepositoryAdapter.save(member);

        return MemberSignupResult.from(saved);
    }
}
