package com.community.admin.api.dto.response;

import com.community.admin.application.dto.AdminPostVisibilityUpdateResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminPostVisibilityUpdateResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminPostVisibilityUpdateResult adminPostVisibilityUpdateResult;
    public static AdminPostVisibilityUpdateResponse from(AdminPostVisibilityUpdateResult adminPostVisibilityUpdateResult, String message) {
        return new AdminPostVisibilityUpdateResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminPostVisibilityUpdateResult
        );
    }
}
