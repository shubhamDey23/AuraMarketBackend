package com.auramarket.tenant.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StorefrontConfigResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private TenantInfoDto tenant;
    private BrandingDto branding;
    private Map<String, Boolean> features;
    private List<MenuItemDto> menus;
    private Map<String, String> settings;
}
