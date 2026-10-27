package com.auramarket.catalog.domain.repository;

import com.auramarket.catalog.domain.model.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
    List<Brand> findByTenantIdAndIsActiveTrue(String tenantId);
    Optional<Brand> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Brand> findByTenantIdAndSlug(String tenantId, String slug);
    boolean existsByTenantIdAndSlug(String tenantId, String slug);
}
