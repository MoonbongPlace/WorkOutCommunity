package com.community.member.application;

import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.application.dto.DeletedMemberResult;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public DetailMemberResult getMemberProfile(Long id) {
        Member member = memberRepositoryAdapter.findById(id)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        return DetailMemberResult.from(member);
    }

    @Transactional
    public UpdatedMemberResult updateProfile(UpdateMemberRequest request, Long id) {
        Member member = memberRepositoryAdapter.findById(id)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String encodedPassword = passwordEncoder.encode(request.getPassword());
        member.updateMember(request, encodedPassword);

        return UpdatedMemberResult.from(member);
    }

    @Transactional
    public DeletedMemberResult withDrawProfile(Long memberId) {
        Member member = memberRepositoryAdapter.findById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        member.deleteMember();

        return DeletedMemberResult.from(member);
    }
}
