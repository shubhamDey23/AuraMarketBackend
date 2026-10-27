package com.auramarket.seller.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardSellerRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotBlank(message = "storeName is required")
    private String storeName;

    @NotBlank(message = "contactEmail is required")
    @Email(message = "Invalid contactEmail format")
    private String contactEmail;

    private String contactPhone;
    private String businessName;
    private String taxId;
    private String businessAddress;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private String description;
}
