package com.community.notification.infra.persistence;

import com.community.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    long countByMemberIdAndIsReadFalse(Long memberId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update Notification n
           set n.isRead = true,
               n.readAt = :readAt
         where n.memberId = :memberId
           and n.isRead = false
    """)
    int markAllRead(@Param("memberId") Long memberId,
                    @Param("readAt") OffsetDateTime readAt);
}
