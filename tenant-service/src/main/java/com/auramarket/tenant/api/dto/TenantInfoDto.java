package com.auramarket.tenant.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TenantInfoDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String tenantCode;
    private String name;
    private String slug;
    private String businessType;
    private String status;
    private String currency;
    private String locale;
    private String timezone;
}
