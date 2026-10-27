package com.auramarket.catalog.domain.repository;

import com.auramarket.catalog.domain.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
    List<Category> findByTenantIdAndIsActiveTrueOrderByDisplayOrderAsc(String tenantId);
    List<Category> findByTenantIdAndParentIdAndIsActiveTrueOrderByDisplayOrderAsc(String tenantId, UUID parentId);
    Optional<Category> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Category> findByTenantIdAndSlug(String tenantId, String slug);
    boolean existsByTenantIdAndSlug(String tenantId, String slug);
}
