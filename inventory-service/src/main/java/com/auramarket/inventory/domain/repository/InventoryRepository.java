package com.auramarket.inventory.domain.repository;

import com.auramarket.inventory.domain.model.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {
    Optional<Inventory> findByTenantIdAndProductId(String tenantId, UUID productId);
    Page<Inventory> findByTenantId(String tenantId, Pageable pageable);
    Page<Inventory> findByTenantIdAndSellerId(String tenantId, UUID sellerId, Pageable pageable);
    boolean existsByTenantIdAndProductId(String tenantId, UUID productId);
}
