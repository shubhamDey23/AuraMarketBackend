package com.auramarket.inventory.domain.repository;

import com.auramarket.inventory.domain.model.InventoryReservation;
import com.auramarket.inventory.domain.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryReservationRepository extends JpaRepository<InventoryReservation, UUID> {
    Optional<InventoryReservation> findByIdAndTenantId(UUID id, String tenantId);
    List<InventoryReservation> findByTenantIdAndProductIdAndStatus(String tenantId, UUID productId, ReservationStatus status);
}
