package com.auramarket.seller.api.dto;

import com.auramarket.seller.domain.model.SellerStatus;
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
public class SellerDto {
    private UUID id;
    private String tenantId;
    private UUID userId;
    private String storeName;
    private String slug;
    private String contactEmail;
    private String contactPhone;
    private String businessName;
    private String taxId;
    private String businessAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private SellerStatus status;
    private String logoUrl;
    private String bannerUrl;
    private String description;
    private BigDecimal rating;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
