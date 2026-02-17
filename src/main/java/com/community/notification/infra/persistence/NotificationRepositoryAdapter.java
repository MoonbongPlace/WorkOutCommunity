package com.community.notification.infra.persistence;

import com.community.notification.domain.model.Notification;
import com.community.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryAdapter implements NotificationRepository {

    private final NotificationJpaRepository notificationJpaRepository;

    @Override
    public Page<Notification> findByMemberIdOrderByCreatedAtDesc(Long memberId, Pageable pageable) {
        return notificationJpaRepository.findByMemberIdOrderByCreatedAtDesc(memberId, pageable);
    }

    @Override
    public long countByMemberIdAndIsReadFalse(Long memberId) {
        return notificationJpaRepository.countByMemberIdAndIsReadFalse(memberId);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id);
    }

    @Override
    public int markAllRead(Long memberId, OffsetDateTime readAt) {
        return notificationJpaRepository.markAllRead(memberId, readAt);
    }

    @Override
    public Notification save(Notification notification) {
        return notificationJpaRepository.save(notification);
    }
}
