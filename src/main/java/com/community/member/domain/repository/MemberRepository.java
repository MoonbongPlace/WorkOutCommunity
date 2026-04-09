package com.community.member.domain.repository;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import jakarta.validation.constraints.NotBlank;
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

    Optional<Member> findActiveByEmail(@NotBlank String email);

    Optional<Member> findActiveById(Long memberId);

    Optional<String> findMemberNameByIdAndStatus(Long senderId);

    List<Member> findAllByStatus(MemberStatus status);

    Page<Member> findByStatus(MemberStatus status, Pageable pageable);

    boolean existsByEmailAndStatus(@NotBlank String email);

    List<Member> findAllByIdIn(List<Long> memberIds);

    // 닉네임으로 활성 멤버 검색
    List<Member> findAllActiveByMemberNameContaining(String memberName);

    Optional<Member> findActiveByPhoneNumberAndName(@NotBlank String phoneNumber, @NotBlank String name);
}
