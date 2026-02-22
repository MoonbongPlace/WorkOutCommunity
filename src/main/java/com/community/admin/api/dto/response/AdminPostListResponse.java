package com.community.admin.api.dto.response;

import com.community.admin.application.dto.AdminPostListResult;
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
public class AdminPostListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private AdminPostListResult adminPostListResult;

    public static AdminPostListResponse from(AdminPostListResult adminPostListResult, String message) {
        return new AdminPostListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                adminPostListResult
        );
    }
}
