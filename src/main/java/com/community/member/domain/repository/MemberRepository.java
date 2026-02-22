package com.community.member.domain.repository;

import com.community.member.domain.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Member findByEmail(String email);

    Page<Member> findAll(Pageable pageable);

    List<Long> findAllActiveMemberIds();
}
