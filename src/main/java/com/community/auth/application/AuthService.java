package com.community.auth.application;

import com.community.auth.api.dto.request.ReissueRequest;
import com.community.auth.api.dto.request.SigninRequest;
import com.community.auth.api.dto.request.SignupRequest;
import com.community.auth.api.dto.response.ReissueResponse;
import com.community.auth.application.dto.MemberSigninResult;
import com.community.auth.application.dto.MemberSignupResult;
import com.community.global.CommonException;
import com.community.global.JWTProvider;
import com.community.global.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
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
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

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
                "user",
                OffsetDateTime.now(),
                MemberStatus.ACTIVE
        );

        Member saved = memberRepositoryAdapter.save(member);

        return MemberSignupResult.from(saved);
    }

    @Transactional
    public MemberSigninResult signin(@Valid SigninRequest request)  {
        Member member = memberRepositoryAdapter.findByEmail(request.getEmail());

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }
        Long memberId = member.getId();

        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole());
        String refreshToken = refreshTokenService.issueAndStore(memberId);

        return MemberSigninResult.from(accessToken, refreshToken, member);

    }

    public ReissueResponse reissue(@Valid ReissueRequest request) {
        String newRefreshToken = refreshTokenService.rotate(request.getRefreshToken());

        Long memberId = jwtProvider.extractMemberId(newRefreshToken);

        String role = jwtProvider.extractRole(newRefreshToken);

        String newAccessToken = jwtProvider.issueAccessToken(memberId, role);

        return ReissueResponse.from("Bearer", newAccessToken, newRefreshToken, jwtProvider.getAtExpSeconds(), jwtProvider.getRtExpSeconds());
    }

    public void logout(String refreshTokenRaw) {
        Long memberId = jwtProvider.extractMemberId(refreshTokenRaw);
        refreshTokenService.revokeAll(memberId);
    }
}
