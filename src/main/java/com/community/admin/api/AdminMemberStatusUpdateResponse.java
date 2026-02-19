package com.community.admin.api;

import com.community.admin.application.AdminMemberStatusUpdateResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminMemberStatusUpdateResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminMemberStatusUpdateResult adminMemberStatusUpdateResult;

    public static AdminMemberStatusUpdateResponse from(AdminMemberStatusUpdateResult adminMemberStatusUpdateResult, String message) {
        return new AdminMemberStatusUpdateResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminMemberStatusUpdateResult
        );
    }
}
