package com.community.member.api.dto.response;

import com.community.member.application.dto.DetailMemberResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private DetailMemberResult detailMember;

    public static MemberInfoResponse get(DetailMemberResult detailMember, String message) {
        return new MemberInfoResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                detailMember
        );
    }
}
