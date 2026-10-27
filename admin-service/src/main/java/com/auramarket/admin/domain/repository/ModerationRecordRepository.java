package com.auramarket.admin.domain.repository;

import com.auramarket.admin.domain.model.ModerationRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ModerationRecordRepository extends JpaRepository<ModerationRecord, UUID> {
    Page<ModerationRecord> findByTenantId(String tenantId, Pageable pageable);
    Page<ModerationRecord> findByTenantIdAndResourceType(String tenantId, String resourceType, Pageable pageable);
}
