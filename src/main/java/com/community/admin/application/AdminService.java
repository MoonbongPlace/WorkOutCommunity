package com.community.admin.application;

import com.community.admin.api.AdminMemberStatusUpdateRequest;
import com.community.global.CommonException;
import com.community.global.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;

    @Transactional(readOnly = true)
    public AdminMemberListResult list(Pageable pageable) {
        Page<Member> page = memberRepositoryAdapter.findAll(pageable);

        return AdminMemberListResult.from(page);
    }

    @Transactional
    public AdminMemberStatusUpdateResult updateStatus(Long memberId, AdminMemberStatusUpdateRequest request) {
        Member member = memberRepositoryAdapter.findById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        if (member.getDeletedAt() != null && request.getStatus() != MemberStatus.DELETED) {
            throw new CommonException(ResponseCode.MEMBER_ALREADY_DELETED);
        }

        member.changeStatus(request.getStatus());

        return AdminMemberStatusUpdateResult.from(member);
    }
}
