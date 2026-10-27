package com.auramarket.tenant.api.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTenantRequest {
    private String name;
    private String businessType;
    private String status;
    private String defaultCurrency;
    private String defaultLocale;
    private String timezone;
}
