package com.community.member.infra.persistence;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);


    @Query("""
                SELECT m.id
                FROM Member m
                WHERE m.status = :status
                  AND m.deletedAt IS NULL
            """)
    List<Long> findAllActiveMemberIds(@Param("status") MemberStatus status);

    @Query("""
            select m from Member m
            where m.email = :email
            and m.status <> :status
            and m.deletedAt is null
            """)
    Optional<Member> findActiveByEmail(@Param("email") String email, @Param("status") MemberStatus status);

    @Query("""
            select m from Member m
            where m.id = :id
            and m.status <> :status
            and m.deletedAt is null
            """)
    Optional<Member> findByIdAndStatusNotAndDeletedAtIsNull(@Param("id") Long id, @Param("status") MemberStatus status);

    @Query("""
    select m.memberName
    from Member m
    where m.id = :memberId
      and m.status = :status
""")
    Optional<String> findMemberNameByIdAndStatus(@Param("memberId") Long memberId,
                                                 @Param("status") MemberStatus status);
}
