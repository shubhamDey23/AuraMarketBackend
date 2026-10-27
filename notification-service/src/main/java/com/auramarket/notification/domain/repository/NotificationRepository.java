package com.auramarket.notification.domain.repository;

import com.auramarket.notification.domain.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    Page<Notification> findByTenantIdAndRecipientId(String tenantId, UUID recipientId, Pageable pageable);
    Optional<Notification> findByIdAndTenantId(UUID id, String tenantId);
    boolean existsByEventId(String eventId);
}
