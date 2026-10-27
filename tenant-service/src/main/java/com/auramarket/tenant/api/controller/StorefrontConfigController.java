package com.auramarket.tenant.api.controller;

import com.auramarket.tenant.api.dto.StorefrontConfigResponse;
import com.auramarket.tenant.application.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/storefront")
@RequiredArgsConstructor
@Tag(name = "Storefront Config API", description = "Dynamic tenant configuration and branding endpoint")
@CrossOrigin(origins = "*")
public class StorefrontConfigController {

    private final TenantService tenantService;

    @GetMapping("/config")
    @Operation(summary = "Get Storefront Configuration", description = "Retrieves dynamic tenant branding, currency, features, menus, and settings")
    public ResponseEntity<StorefrontConfigResponse> getStorefrontConfig(
            @RequestParam(name = "tenantCode", defaultValue = "AURA_AYURVEDA") String tenantCode) {
        StorefrontConfigResponse config = tenantService.getStorefrontConfig(tenantCode);
        return ResponseEntity.ok(config);
    }
}
