package com.auramarket.tenant.domain.repository;

import com.auramarket.tenant.domain.model.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {
    Optional<Tenant> findByTenantCode(String tenantCode);
    Optional<Tenant> findBySlug(String slug);
    boolean existsByTenantCode(String tenantCode);
}
