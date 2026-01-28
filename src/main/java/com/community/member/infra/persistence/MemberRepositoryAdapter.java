package com.community.member.infra.persistence;

import com.community.board.infra.persistence.PostJpaRepository;
import com.community.member.domain.model.Member;
import com.community.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryAdapter implements MemberRepository {

    private final PostJpaRepository postJpaRepository;

    @Override
    public Member save(Member member) {
        return postJpaRepository.save(member);
    }

    @Override
    public Optional<Member> findById(Long id) {
        return postJpaRepository.findById(id);
    }
}
