package com.community.admin.application.dto;

import com.community.member.domain.model.Member;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"content","page","size","totalElements","totalPages","last"})
public class AdminMemberListResult {
    private List<AdminMemberListItem> content;

    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;

    public static AdminMemberListResult from(Page<Member> page) {
        return new AdminMemberListResult(
                page.getContent().stream().map(AdminMemberListItem::from).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }
}
