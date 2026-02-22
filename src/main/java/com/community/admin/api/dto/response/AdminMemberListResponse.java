package com.community.admin.api.dto.response;

import com.community.admin.application.dto.AdminMemberListResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminMemberListResult adminMemberListResult;

    public static AdminMemberListResponse from(AdminMemberListResult adminMemberListResult, String message) {
        return new AdminMemberListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminMemberListResult
        );
    }
}
