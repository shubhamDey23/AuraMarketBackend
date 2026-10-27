package com.auramarket.tenant.domain.repository;

import com.auramarket.tenant.domain.model.TenantFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantFeatureRepository extends JpaRepository<TenantFeature, UUID> {
    List<TenantFeature> findByTenantId(UUID tenantId);
    Optional<TenantFeature> findByTenantIdAndFeatureCode(UUID tenantId, String featureCode);
}

