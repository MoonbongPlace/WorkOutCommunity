package com.community.auth.application;

import com.community.auth.api.dto.request.FindUserIdRequest;
import com.community.auth.api.dto.request.PasswordResetRequest;
import com.community.auth.api.dto.request.VerifyRequest;
import com.community.auth.api.dto.request.VerifyResultRequest;
import com.community.auth.api.dto.response.PasswordResetResponse;
import com.community.auth.api.dto.response.VerifyResultResponse;
import com.community.auth.application.dto.FindUserIdResult;
import com.community.auth.application.dto.PhoneVerifyResult;
import com.community.auth.domain.PasswordResetToken;
import com.community.auth.domain.PhoneVerification;
import com.community.auth.infra.PasswordResetTokenRepositoryAdapter;
import com.community.auth.infra.PhoneVerificationRepositoryAdapter;
import com.community.global.component.SesMailSender;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.global.solapi.SolapiMessageService;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.Base64;

import static com.community.global.jwt.TokenHashUtil.sha256;

@Service
@RequiredArgsConstructor
public class AccountRecoveryService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PhoneVerificationRepositoryAdapter phoneVerificationRepositoryAdapter;
    private final PasswordResetTokenRepositoryAdapter passwordResetTokenRepositoryAdapter;
    private final SolapiMessageService solapiMessageService;
    private final SesMailSender mailSender;
    private final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64UrlEncoder =
            Base64.getUrlEncoder().withoutPadding();

    @Value("${app.frontend-base-url}")
    private String frontendBaseUrl;

    // 아이디 찾기
    public FindUserIdResult findMember(@Valid FindUserIdRequest request) {
        Member member = memberRepositoryAdapter.findActiveByPhoneNumberAndName(request.getPhoneNumber(), request.getName())
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        return FindUserIdResult.of(member.getName(), member.getEmail());
    }

    // 인증번호 발송
    @Transactional
    public PhoneVerifyResult sendVerificationCode(@Valid VerifyRequest request) {
        String verificationNumber = generateCode();
        String phoneNumber = request.getPhoneNumber().replaceAll("[^0-9]", "");

        PhoneVerification phoneVerification = phoneVerificationRepositoryAdapter
                .findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (phoneVerification == null) {
            phoneVerification = PhoneVerification.create(phoneNumber, verificationNumber);
        } else {
            phoneVerification.update(verificationNumber);
        }

        PhoneVerification saved = phoneVerificationRepositoryAdapter.save(phoneVerification);
        solapiMessageService.sendVerificationSms(phoneNumber, verificationNumber);

        return PhoneVerifyResult.from(saved);
    }

    // 인증번호 검증
    @Transactional
    public VerifyResultResponse verifyVerificationCode(@Valid VerifyResultRequest request) {
        PhoneVerification phoneVerification = phoneVerificationRepositoryAdapter.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new CommonException(ResponseCode.PHONE_VERIFICATION_NOT_FOUND));

        OffsetDateTime now = OffsetDateTime.now();
        if (now.isAfter(phoneVerification.getExpiredAt())) {
            throw new CommonException(ResponseCode.EXPIRED_PHONE_VERIFICATION);
        }
        boolean isMatch = phoneVerification.getVerificationCode().equals(request.getVerificationCode());

        if (!isMatch) {
            throw new CommonException(ResponseCode.INVALID_PHONE_VERIFICATION_CODE);
        }
        phoneVerificationRepositoryAdapter.deleteById(phoneVerification.getId());

        return VerifyResultResponse.of("번호 인증 성공");
    }

    // 비밀번호 초기화 요청
    @Transactional
    public PasswordResetResponse requestPasswordReset(PasswordResetRequest request) {
        String email = request.getEmail().trim();
        String name = request.getName().trim();

        memberRepositoryAdapter.findActiveByEmailAndName(email, name)
                .ifPresent(this::issuePasswordResetTokenAndSendEmail);

        return PasswordResetResponse.of("입력하신 정보가 일치하면 비밀번호 재설정 메일이 발송됩니다.");
    }

    private void issuePasswordResetTokenAndSendEmail(Member member) {
        passwordResetTokenRepositoryAdapter.deleteAllByMemberId(member.getId());

        String rawToken = generateResetToken();
        String tokenHash = sha256(rawToken);
        OffsetDateTime expiredAt = OffsetDateTime.now().plusMinutes(15);

        PasswordResetToken passwordResetToken = PasswordResetToken.create(
                member.getId(),
                tokenHash,
                expiredAt
        );

        passwordResetTokenRepositoryAdapter.save(passwordResetToken);

        String resetLink = frontendBaseUrl + "/reset-password?token=" + rawToken;
        String subject = "[WorkOutCommunity] 비밀번호 재설정 안내";
        String htmlContent = buildPasswordResetHtml(resetLink);

        mailSender.send(member.getEmail(), subject, htmlContent);
    }

    private String buildPasswordResetHtml(String resetLink) {
        return """
        <html>
          <body>
            <h2>비밀번호 재설정</h2>
            <p>아래 버튼을 눌러 비밀번호를 재설정하세요.</p>
            <a href="%s">비밀번호 재설정</a>
            <p>이 링크는 15분 후 만료됩니다.</p>
          </body>
        </html>
        """.formatted(resetLink);
    }

    // 인증 번호 생성
    public String generateCode() {
        int number = secureRandom.nextInt(100000);

        return String.format("%07d", number);
    }

    // 비밀번호 초기화 토큰 생성
    public String generateResetToken(){
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64UrlEncoder.encodeToString(randomBytes);
    }
}
