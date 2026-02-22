package com.community.member.infra.persistence;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import com.community.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return memberJpaRepository.findAll(pageable);
    }

    @Override
    public List<Long> findAllActiveMemberIds() {
        return memberJpaRepository.findAllActiveMemberIds(MemberStatus.ACTIVE);
    }
}
