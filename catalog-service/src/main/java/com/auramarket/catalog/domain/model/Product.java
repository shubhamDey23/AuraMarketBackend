package com.auramarket.catalog.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "tenant_id", nullable = false, length = 50)
    private String tenantId;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "category_id")
    private UUID categoryId;

    @Column(name = "subcategory_id")
    private UUID subcategoryId;

    @Column(name = "brand_id")
    private UUID brandId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 200)
    private String slug;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    @Builder.Default
    private ProductType productType = ProductType.PHYSICAL;

    @Column(nullable = false, length = 100)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_price", precision = 12, scale = 2)
    private BigDecimal discountPrice;

    @Builder.Default
    @Column(length = 10, nullable = false)
    private String currency = "INR";

    @Column(name = "images_json", columnDefinition = "TEXT")
    private String imagesJson;

    @Column(name = "attributes_json", columnDefinition = "TEXT")
    private String attributesJson;

    @Column(name = "tags_json", columnDefinition = "TEXT")
    private String tagsJson;

    @Builder.Default
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    @Builder.Default
    @Column(name = "is_trending", nullable = false)
    private Boolean isTrending = false;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) id = UUID.randomUUID();
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}
