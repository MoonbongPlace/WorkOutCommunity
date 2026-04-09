package com.community.auth.application;

import com.community.auth.api.dto.request.*;
import com.community.auth.api.dto.response.ReissueResponse;
//import com.community.auth.api.dto.response.VerifyResultResponse;
import com.community.auth.application.dto.*;
import com.community.auth.domain.PhoneVerification;
import com.community.auth.infra.PhoneVerificationRepositoryAdapter;
import com.community.global.component.property.ProfileProperties;
import com.community.global.exception.CommonException;
import com.community.global.jwt.JWTProvider;
import com.community.global.exception.ResponseCode;
import com.community.global.solapi.SolapiMessageService;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(ProfileProperties.class)
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PhoneVerificationRepositoryAdapter phoneVerificationRepositoryAdapter;
    private final JWTProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final ProfileProperties profileProperties;
    private final SolapiMessageService solapiMessageService;
    private final SecureRandom secureRandom = new SecureRandom();


    @Transactional
    public MemberSignupResult signup(@Valid SignupRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String profileImageUrl = profileProperties.defaultImageUrl();

        String userNumber = request.getPhoneNumber().replaceAll("[0-9]", "");

        // 휴대전화 11자인지 확인 처리 추가.(예정)

        Member member = Member.signup(
                request.getEmail(),
                userNumber,
                request.getMemberName(),
                encodedPassword,
                request.getName(),
                request.getAge(),
                request.getSex(),
                "user",
                OffsetDateTime.now(),
                MemberStatus.ACTIVE,
                profileImageUrl
        );

        Member saved = memberRepositoryAdapter.save(member);

        return MemberSignupResult.from(saved);
    }

    @Transactional
    public MemberSigninResult signin(@Valid SigninRequest request)  {
        Member member = memberRepositoryAdapter.findActiveByEmail(request.getEmail())
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));
        boolean isSuspend = memberRepositoryAdapter.existsByEmailAndStatus(request.getEmail());

        if (isSuspend){
            throw new CommonException(ResponseCode.MEMBER_SUSPENDED);
        }

        if(!passwordEncoder.matches(request.getPassword(), member.getPassword())){
            throw new CommonException(ResponseCode.INVALID_PASSWORD);
        }
        Long memberId = member.getId();

        UUID sessionId = UUID.randomUUID();

        String accessToken = jwtProvider.issueAccessToken(memberId, member.getRole());
        String refreshToken = refreshTokenService.issueAndStore(memberId, sessionId);

        return MemberSigninResult.from(accessToken, refreshToken, member);

    }

    public ReissueResponse reissue(String refreshTokenRaw) {
        Long memberId = jwtProvider.extractMemberId(refreshTokenRaw);

        String newRefreshToken = refreshTokenService.rotate(refreshTokenRaw);

        Member member = memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String role = member.getRole();

        String newAccessToken = jwtProvider.issueAccessToken(memberId, role);

        return ReissueResponse.from("Bearer", newAccessToken, newRefreshToken, jwtProvider.getAtExpSeconds(), jwtProvider.getRtExpSeconds());
    }

    public void logout(String refreshTokenRaw) {
        Long memberId = jwtProvider.extractMemberId(refreshTokenRaw);
        refreshTokenService.revokeAll(memberId);
    }

    @Transactional
    public PhoneVerifyResult verify(@Valid VerifyRequest request) {

        String verificationNumber = generateCode();
        PhoneVerification phoneVerification = PhoneVerification.create(request.getPhoneNumber(), verificationNumber );

        PhoneVerification saved = phoneVerificationRepositoryAdapter.save(phoneVerification);

        solapiMessageService.sendVerificationSms(request.getPhoneNumber(), verificationNumber);

        return PhoneVerifyResult.from(saved);
    }

    public EmailVerifyResult emailPersonalCode(@Valid EmailRequest request) {
        return null;
    }

    public FindUserIdResult findUserId(@Valid FindUserIdRequest request) {
        Member member = memberRepositoryAdapter.findActiveByPhoneNumberAndName(request.getPhoneNumber(), request.getName())
                .orElseThrow(()->new CommonException(ResponseCode.MEMBER_NOT_FOUND));


        return FindUserIdResult.of(member.getName(), member.getEmail());
    }

    public String generateCode(){
        int number = secureRandom.nextInt(100000);

        return String.format("%07d", number);
    }

//    public VerifyResultResponse verifyResult(@Valid VerifyResultRequest request) {
//        return null;
//    }
}
