package com.community.admin.api.dto.response;

import com.community.admin.application.AdminMemberDetailResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberDetailResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminMemberDetailResult adminMemberDetailResult;

    public static AdminMemberDetailResponse from(AdminMemberDetailResult adminMemberDetailResult, String message) {
        return new AdminMemberDetailResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminMemberDetailResult
        );
    }
}
