package com.community.admin.application.dto;

import com.community.board.domain.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
public class AdminPostListResult {
    List<AdminPostListItem> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static AdminPostListResult from(Page<Post> page) {
        return new AdminPostListResult(
                page.getContent().stream().map(AdminPostListItem::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
