package com.community.category.api.dto.response;

import com.community.category.application.dto.CategoryListResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@Getter
@AllArgsConstructor
public class CategoryListResponse {
    private String code;
    private String message;
    private OffsetDateTime timestamp;
    private CategoryListResult categoryListResult;

    public static CategoryListResponse from(CategoryListResult categoryListResult, String message) {
        return new CategoryListResponse(
                String.valueOf(HttpStatus.OK.value()),
                message,
                OffsetDateTime.now(),
                categoryListResult
        );
    }
}
