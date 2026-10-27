package com.auramarket.seller.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSellerRequest {
    private String storeName;
    private String contactEmail;
    private String contactPhone;
    private String businessName;
    private String taxId;
    private String businessAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String logoUrl;
    private String bannerUrl;
    private String description;
}
