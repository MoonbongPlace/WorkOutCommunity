package com.community.member.application;

import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.application.dto.DeletedMemberResult;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryAdapter memberRepositoryAdapter;

    @Transactional(readOnly = true)
    public DetailMemberResult getMemberProfile(Long id) {

        Member member = memberRepositoryAdapter.findById(id).orElseThrow();
        return DetailMemberResult.from(member);
    }

    @Transactional
    public UpdatedMemberResult updateProfile(UpdateMemberRequest request, Long id) {
        Member member = memberRepositoryAdapter.findById(id).orElseThrow();

        member.setMemberName(request.getMemberName());
        member.setPassword(request.getPassword());
        member.setName(request.getName());
        member.setAge(request.getAge());
        member.setSex(request.getSex());

        Member saved = memberRepositoryAdapter.save(member);

        return UpdatedMemberResult.from(saved);
    }

    @Transactional
    public DeletedMemberResult withDrawProfile(Long memberId) {
        Member member = memberRepositoryAdapter.findById(memberId).orElseThrow();

        member.setDeletedAt(OffsetDateTime.now());

        Member saved = memberRepositoryAdapter.save(member);

        return DeletedMemberResult.from(saved);
    }
}
