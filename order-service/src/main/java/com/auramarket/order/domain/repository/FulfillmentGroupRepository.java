package com.auramarket.order.domain.repository;

import com.auramarket.order.domain.model.FulfillmentGroup;
import com.auramarket.order.domain.model.FulfillmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FulfillmentGroupRepository extends JpaRepository<FulfillmentGroup, UUID> {
    List<FulfillmentGroup> findByOrderId(UUID orderId);
    Page<FulfillmentGroup> findBySellerId(UUID sellerId, Pageable pageable);
    Page<FulfillmentGroup> findBySellerIdAndStatus(UUID sellerId, FulfillmentStatus status, Pageable pageable);
    Optional<FulfillmentGroup> findByIdAndSellerId(UUID id, UUID sellerId);
}
