package com.community.member.domain.repository;

import com.community.member.domain.model.Member;

import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Member findByEmail(String email);
}
