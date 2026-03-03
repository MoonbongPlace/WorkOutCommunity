package com.community.category.application.dto;

import com.community.category.domain.model.Category;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"categoryList"})
public class CategoryListResult {
    private List<Item> categoryList;

    @Getter
    @AllArgsConstructor
    @JsonPropertyOrder({"id", "name"})
    private static class Item{
        private Long id;
        private String name;
    }

    public static CategoryListResult of(List<Category> list) {
        List<Item> mappedList = list.stream()
                .map(item -> new Item(
                        item.getId(),
                        item.getName()
                ))
                .toList();
        return new CategoryListResult(mappedList);
    }
}
