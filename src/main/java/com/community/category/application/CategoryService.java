package com.community.category.application;

import com.community.category.application.dto.CategoryListResult;
import com.community.category.domain.model.Category;
import com.community.category.infra.persistance.CategoryRepositoryAdapter;
import com.community.global.exception.CommonException;
import com.community.global.exception.ResponseCode;
import com.community.member.domain.model.Member;
import com.community.member.infra.persistence.MemberRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final MemberRepositoryAdapter memberRepositoryAdapter;
    private final CategoryRepositoryAdapter categoryRepositoryAdapter;

    public CategoryListResult getList(Long memberId) {
        Member member = memberRepositoryAdapter.findById(memberId)
                .orElseThrow(()-> new CommonException(ResponseCode.MEMBER_NOT_FOUND));

        List<Category> list = categoryRepositoryAdapter.findAll();
        return CategoryListResult.of(list);
    }
}
