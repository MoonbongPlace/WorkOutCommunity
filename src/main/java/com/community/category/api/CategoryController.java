package com.community.category.api;

import com.community.category.api.dto.response.CategoryListResponse;
import com.community.category.application.dto.CategoryListResult;
import com.community.category.application.CategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category", description = "카테고리 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<CategoryListResponse> getCategoryList() {
        CategoryListResult categoryListResult = categoryService.getList();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CategoryListResponse.from(categoryListResult, "카테고리 리스트 조회 성공"));
    }
}
