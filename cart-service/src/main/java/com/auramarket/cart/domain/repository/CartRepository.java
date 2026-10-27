package com.auramarket.cart.domain.repository;

import com.auramarket.cart.domain.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartRepository extends JpaRepository<Cart, UUID> {
    Optional<Cart> findByTenantIdAndCustomerId(String tenantId, UUID customerId);
}
