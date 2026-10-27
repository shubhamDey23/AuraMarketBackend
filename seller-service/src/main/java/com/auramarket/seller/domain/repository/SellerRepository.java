package com.auramarket.seller.domain.repository;

import com.auramarket.seller.domain.model.Seller;
import com.auramarket.seller.domain.model.SellerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SellerRepository extends JpaRepository<Seller, UUID> {
    Optional<Seller> findByIdAndTenantId(UUID id, String tenantId);
    Optional<Seller> findByTenantIdAndUserId(String tenantId, UUID userId);
    Optional<Seller> findByTenantIdAndSlug(String tenantId, String slug);
    Page<Seller> findByTenantId(String tenantId, Pageable pageable);
    Page<Seller> findByTenantIdAndStatus(String tenantId, SellerStatus status, Pageable pageable);
    boolean existsByTenantIdAndUserId(String tenantId, UUID userId);
    boolean existsByTenantIdAndSlug(String tenantId, String slug);
}
