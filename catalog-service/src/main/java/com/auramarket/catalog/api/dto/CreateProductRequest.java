package com.auramarket.catalog.api.dto;

import com.auramarket.catalog.domain.model.ProductType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateProductRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotNull(message = "sellerId is required")
    private UUID sellerId;

    private UUID categoryId;
    private UUID subcategoryId;
    private UUID brandId;

    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "sku is required")
    private String sku;

    private String description;
    private String shortDescription;

    @Builder.Default
    private ProductType productType = ProductType.PHYSICAL;

    @NotNull(message = "price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0")
    private BigDecimal price;

    private BigDecimal discountPrice;

    @Builder.Default
    private String currency = "INR";

    private String imagesJson;
    private String attributesJson;
    private String tagsJson;
}
