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

    @Override
    public Optional<Member> findActiveByEmail(String email) {
        return memberJpaRepository.findActiveByEmail(email, MemberStatus.DELETED);
    }

    @Override
    public Optional<Member> findActiveById(Long memberId) {
        return memberJpaRepository.findByIdAndStatusNotAndDeletedAtIsNull(memberId, MemberStatus.DELETED);
    }

    @Override
    public Optional<String> findMemberNameByIdAndStatus(Long senderId) {
            return memberJpaRepository.findMemberNameByIdAndStatus(senderId, MemberStatus.ACTIVE);
    }

    @Override
    public List<Member> findAllByStatus(MemberStatus status) {
        return memberJpaRepository.findAllByStatus(status);
    }

    @Override
    public Page<Member> findByStatus(MemberStatus status, Pageable pageable) {
        return memberJpaRepository.findByStatus(status, pageable);
    }

    @Override
    public boolean existsByEmailAndStatus(String email) {
        return memberJpaRepository.existsByEmailAndStatus(email, MemberStatus.SUSPENDED);
    }

    @Override
    public List<Member> findAllByIdIn(List<Long> memberIds) {
        return memberJpaRepository.findAllByIdIn(memberIds);
    }

    @Override
    public List<Member> findAllActiveByMemberNameContaining(String memberName) {
        return memberJpaRepository.findAllActiveByMemberNameContaining(memberName);
    }
}
