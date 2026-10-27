package com.auramarket.admin.domain.repository;

import com.auramarket.admin.domain.model.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    Page<AuditLog> findByTenantId(String tenantId, Pageable pageable);
    Page<AuditLog> findByTenantIdAndActorUserId(String tenantId, UUID actorUserId, Pageable pageable);
}
