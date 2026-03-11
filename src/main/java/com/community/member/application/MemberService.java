package com.community.member.application;

import com.community.global.component.ImageStorage;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.api.dto.request.UpdateMemberRequest;
import com.community.member.application.dto.DeletedMemberResult;
import com.community.member.application.dto.DetailMemberResult;
import com.community.member.application.dto.UpdatedMemberResult;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final ImageStorage imageStorage;

    @Transactional(readOnly = true)
    public DetailMemberResult getMemberProfile(Long id) {
        Member member = memberRepositoryAdapter.findActiveById(id)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        return DetailMemberResult.from(member);
    }

    @Transactional
    public UpdatedMemberResult updateProfile(UpdateMemberRequest request, Long id, MultipartFile profileImage) {
        Member member = memberRepositoryAdapter.findActiveById(id)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        String profileImageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = imageStorage.store(profileImage);
        }
        member.updateMember(request, profileImageUrl);

        return UpdatedMemberResult.from(member);
    }

    @Transactional
    public DeletedMemberResult withDrawProfile(Long memberId) {
        Member member = memberRepositoryAdapter.findActiveById(memberId)
                .orElseThrow(() -> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        member.deleteMember();

        return DeletedMemberResult.from(member);
    }
}
