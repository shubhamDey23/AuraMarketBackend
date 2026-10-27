package com.auramarket.tenant.domain.repository;

import com.auramarket.tenant.domain.model.TenantMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TenantMenuRepository extends JpaRepository<TenantMenu, UUID> {
    List<TenantMenu> findByTenantIdOrderByDisplayOrderAsc(UUID tenantId);
    List<TenantMenu> findByTenantIdAndIsVisibleTrueOrderByDisplayOrderAsc(UUID tenantId);
    void deleteByTenantIdAndId(UUID tenantId, UUID id);
}

