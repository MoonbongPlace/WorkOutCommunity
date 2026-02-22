package com.community.admin.api.dto.response;

import com.community.admin.application.AdminPostDetailResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class AdminPostDetailResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminPostDetailResult adminPostDetailResult;

    public static AdminPostDetailResponse from(AdminPostDetailResult adminPostDetailResult, String message) {
        return new AdminPostDetailResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminPostDetailResult
        );
    }
}
