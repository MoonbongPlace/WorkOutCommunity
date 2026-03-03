package com.community.category.domain.repository;

import com.community.category.domain.model.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> findAll();
}
