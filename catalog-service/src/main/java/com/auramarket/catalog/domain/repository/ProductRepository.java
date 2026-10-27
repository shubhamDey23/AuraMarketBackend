package com.auramarket.catalog.domain.repository;

import com.auramarket.catalog.domain.model.Product;
import com.auramarket.catalog.domain.model.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    Optional<Product> findByIdAndTenantId(UUID id, String tenantId);

    Optional<Product> findByTenantIdAndSlug(String tenantId, String slug);

    Page<Product> findByTenantIdAndStatus(String tenantId, ProductStatus status, Pageable pageable);

    Page<Product> findByTenantIdAndSellerId(String tenantId, UUID sellerId, Pageable pageable);

    boolean existsByTenantIdAndSku(String tenantId, String sku);

    boolean existsByTenantIdAndSlug(String tenantId, String slug);
}
