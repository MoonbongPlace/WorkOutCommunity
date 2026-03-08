package com.community.admin.api.dto.request;

import com.community.member.domain.model.MemberStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminMemberStatusUpdateRequest {
    @NotNull
    MemberStatus status;
    String reason;
}
