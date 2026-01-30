package com.community.member.infra.persistence;

import com.community.member.domain.model.Member;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member, Long> {
}
