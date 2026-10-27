package com.auramarket.catalog.api.dto;

import com.auramarket.catalog.domain.model.ProductStatus;
import com.auramarket.catalog.domain.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;
    private String tenantId;
    private UUID sellerId;
    private UUID categoryId;
    private UUID subcategoryId;
    private UUID brandId;
    private String name;
    private String slug;
    private String description;
    private String shortDescription;
    private ProductType productType;
    private String sku;
    private ProductStatus status;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String currency;
    private String imagesJson;
    private String attributesJson;
    private String tagsJson;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
