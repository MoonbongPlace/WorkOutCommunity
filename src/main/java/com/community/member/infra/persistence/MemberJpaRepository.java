package com.community.member.infra.persistence;

import com.community.member.domain.model.Member;
import com.community.member.domain.model.MemberStatus;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email);


    @Query("""
    SELECT m.id
    FROM Member m
    WHERE m.status = :status
      AND m.deletedAt IS NULL
""")
    List<Long> findAllActiveMemberIds(@Param("status") MemberStatus status);
}
