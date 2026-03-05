package com.community.board.application.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"content","page","size","totalElements","totalPages","last"})
public class PostListResult {
    List<PostListItem> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static PostListResult from(List<PostListItem> items, Page<?> page) {
        return new PostListResult(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
