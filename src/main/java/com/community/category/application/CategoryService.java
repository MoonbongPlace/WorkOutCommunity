package com.community.category.application;

import com.community.category.application.dto.CategoryListResult;
import com.community.category.domain.model.Category;
import com.community.category.infra.persistance.CategoryRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepositoryAdapter categoryRepositoryAdapter;

    public CategoryListResult getList() {
        List<Category> list = categoryRepositoryAdapter.findAll();
        return CategoryListResult.of(list);
    }
}
