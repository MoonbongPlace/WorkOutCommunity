package com.community.admin.application.dto;

import com.community.member.domain.model.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberStatusUpdateResult {
    private Long memberId;
    private String status;
    private OffsetDateTime updatedAt;

    public static AdminMemberStatusUpdateResult from(Member member) {
        return new AdminMemberStatusUpdateResult(
                member.getId(),
                member.getStatus().name(),
                member.getUpdatedAt()
        );
    }
}
