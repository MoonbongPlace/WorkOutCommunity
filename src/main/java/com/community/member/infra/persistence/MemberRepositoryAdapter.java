package com.community.member.infra.persistence;

import com.community.member.domain.model.Member;
import com.community.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public Member save(Member member) {
        return memberJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberJpaRepository.findById(id);
    }

    public Member findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }
}
