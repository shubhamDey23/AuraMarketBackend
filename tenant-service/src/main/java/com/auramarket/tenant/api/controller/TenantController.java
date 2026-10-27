package com.auramarket.tenant.api.controller;

import com.auramarket.tenant.api.dto.StorefrontConfigResponse;
import com.auramarket.tenant.application.service.TenantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Management API", description = "Tenant management and cache eviction endpoints")
@CrossOrigin(origins = "*")
public class TenantController {

    private final TenantService tenantService;

    @GetMapping("/{tenantCode}")
    @Operation(summary = "Get Tenant Details", description = "Retrieves configuration for a specific tenant code")
    public ResponseEntity<StorefrontConfigResponse> getTenantByCode(@PathVariable("tenantCode") String tenantCode) {
        return ResponseEntity.ok(tenantService.getStorefrontConfig(tenantCode));
    }

    @PostMapping("/{tenantCode}/cache/invalidate")
    @Operation(summary = "Invalidate Tenant Cache", description = "Evicts the cached configuration for the given tenant")
    public ResponseEntity<Void> invalidateCache(@PathVariable("tenantCode") String tenantCode) {
        tenantService.invalidateTenantCache(tenantCode);
        return ResponseEntity.noContent().build();
    }
}
