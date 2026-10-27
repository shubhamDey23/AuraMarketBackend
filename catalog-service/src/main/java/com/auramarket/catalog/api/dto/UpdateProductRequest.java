package com.auramarket.catalog.api.dto;

import com.auramarket.catalog.domain.model.ProductStatus;
import com.auramarket.catalog.domain.model.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProductRequest {
    private String name;
    private String description;
    private String shortDescription;
    private UUID categoryId;
    private UUID subcategoryId;
    private UUID brandId;
    private ProductType productType;
    private ProductStatus status;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String currency;
    private String imagesJson;
    private String attributesJson;
    private String tagsJson;
}
