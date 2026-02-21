package com.community.admin.application;

import com.community.admin.api.AdminMemberStatusUpdateRequest;
import com.community.admin.api.AdminPostVisibilityUpdateRequest;
import com.community.board.domain.model.Post;
import com.community.board.domain.model.PostVisibility;
import com.community.board.infra.persistence.PostRepositoryAdapter;
import com.community.global.CommonException;
import com.community.global.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import jakarta.validation.constraints.NotBlank;
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
    private final PostRepositoryAdapter postRepositoryAdapter;

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
        if (request.getStatus()==MemberStatus.DELETED)
            member.setDeletedAt(OffsetDateTime.now());

        return AdminMemberStatusUpdateResult.from(member);
    }

    @Transactional
    public AdminPostVisibilityUpdateResult hiddenPost(PostVisibility visibility, Long postId) {
        Post post = postRepositoryAdapter.findById(postId)
                .orElseThrow(()-> new CommonException(ResponseCode.POST_NOT_FOUND));

        if (post.getDeletedAt()!= null) {
            throw new CommonException(ResponseCode.POST_ALREADY_DELETED);
        }
        if (visibility == PostVisibility.HIDDEN) post.hide();
        else post.show();

        return AdminPostVisibilityUpdateResult.from(post);
    }
}
