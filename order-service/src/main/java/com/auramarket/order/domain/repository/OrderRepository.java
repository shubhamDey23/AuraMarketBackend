package com.auramarket.order.domain.repository;

import com.auramarket.order.domain.model.Order;
import com.auramarket.order.domain.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Order> findByTenantIdAndOrderNumber(String tenantId, String orderNumber);
    Page<Order> findByTenantId(String tenantId, Pageable pageable);
    Page<Order> findByTenantIdAndCustomerId(String tenantId, UUID customerId, Pageable pageable);
    Page<Order> findByTenantIdAndStatus(String tenantId, OrderStatus status, Pageable pageable);
}
