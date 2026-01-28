package com.community.member.application;

import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import com.community.member.domain.MemberRepository;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private MemberRepository memberRepository;
    private MemberRepositoryAdapter memberRepositoryAdapter;

    public DetailMemberResult getMemberProfile(Long id) {

        Member member = memberRepositoryAdapter.findById(id).orElseThrow();
        return DetailMemberResult.from(member);
    }

//    public UpdatedMemberResult update(UpdateMemberRequest request) {
//    }
}
