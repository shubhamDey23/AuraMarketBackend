package com.auramarket.inventory.domain.repository;

import com.auramarket.inventory.domain.model.InventoryTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, UUID> {
    Page<InventoryTransaction> findByInventoryIdOrderByCreatedAtDesc(UUID inventoryId, Pageable pageable);
}
