package com.community.notification.domain.repository;


import com.community.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.OffsetDateTime;
import java.util.Optional;

public interface NotificationRepository {
    Page<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable);

    long countByMemberIdAndIsReadFalse(Long memberId);

    Optional<Notification> findById(Long id);

    int markAllRead(Long memberId, OffsetDateTime readAt);

    Notification save(Notification notification);
}
