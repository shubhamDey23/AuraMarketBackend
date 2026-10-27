package com.auramarket.tenant.api.controller;

import com.auramarket.tenant.api.dto.BrandingDto;
import com.auramarket.tenant.api.dto.MenuItemDto;
import com.auramarket.tenant.api.dto.TenantInfoDto;
import com.auramarket.tenant.api.dto.admin.*;
import com.auramarket.tenant.application.service.TenantService;
import com.auramarket.tenant.domain.model.TenantFeature;
import com.auramarket.tenant.domain.model.TenantSetting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/tenants")
@RequiredArgsConstructor
@Tag(name = "Tenant Admin API", description = "Admin endpoints for managing tenant configurations, branding, menus, features, and settings")
@CrossOrigin(origins = "*")
public class TenantAdminController {

    private final TenantService tenantService;

    @GetMapping
    @Operation(summary = "List all tenants")
    public ResponseEntity<List<TenantInfoDto>> getAllTenants() {
        return ResponseEntity.ok(tenantService.getAllTenants());
    }

    @GetMapping("/{tenantId}")
    @Operation(summary = "Get tenant by ID")
    public ResponseEntity<TenantInfoDto> getTenantById(@PathVariable("tenantId") UUID tenantId) {
        return ResponseEntity.ok(tenantService.getTenantById(tenantId));
    }

    @PostMapping
    @Operation(summary = "Create a new tenant")
    public ResponseEntity<TenantInfoDto> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        TenantInfoDto created = tenantService.createTenant(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{tenantId}")
    @Operation(summary = "Update tenant details")
    public ResponseEntity<TenantInfoDto> updateTenant(
            @PathVariable("tenantId") UUID tenantId,
            @RequestBody UpdateTenantRequest request) {
        return ResponseEntity.ok(tenantService.updateTenant(tenantId, request));
    }

    @PutMapping("/{tenantId}/branding")
    @Operation(summary = "Update tenant branding")
    public ResponseEntity<BrandingDto> updateBranding(
            @PathVariable("tenantId") UUID tenantId,
            @RequestBody UpdateBrandingRequest request) {
        return ResponseEntity.ok(tenantService.updateBranding(tenantId, request));
    }

    @GetMapping("/{tenantId}/menus")
    @Operation(summary = "Get tenant menu items")
    public ResponseEntity<List<MenuItemDto>> getTenantMenus(@PathVariable("tenantId") UUID tenantId) {
        return ResponseEntity.ok(tenantService.getTenantMenus(tenantId));
    }

    @PostMapping("/{tenantId}/menus")
    @Operation(summary = "Create a menu item for tenant")
    public ResponseEntity<MenuItemDto> addMenuItem(
            @PathVariable("tenantId") UUID tenantId,
            @Valid @RequestBody SaveMenuItemRequest request) {
        MenuItemDto created = tenantService.addMenuItem(tenantId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{tenantId}/menus/{menuId}")
    @Operation(summary = "Update a menu item for tenant")
    public ResponseEntity<MenuItemDto> updateMenuItem(
            @PathVariable("tenantId") UUID tenantId,
            @PathVariable("menuId") UUID menuId,
            @Valid @RequestBody SaveMenuItemRequest request) {
        return ResponseEntity.ok(tenantService.updateMenuItem(tenantId, menuId, request));
    }

    @DeleteMapping("/{tenantId}/menus/{menuId}")
    @Operation(summary = "Delete a menu item for tenant")
    public ResponseEntity<Void> deleteMenuItem(
            @PathVariable("tenantId") UUID tenantId,
            @PathVariable("menuId") UUID menuId) {
        tenantService.deleteMenuItem(tenantId, menuId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{tenantId}/features")
    @Operation(summary = "Get tenant features")
    public ResponseEntity<List<TenantFeature>> getTenantFeatures(@PathVariable("tenantId") UUID tenantId) {
        return ResponseEntity.ok(tenantService.getTenantFeatures(tenantId));
    }

    @PutMapping("/{tenantId}/features/{featureCode}")
    @Operation(summary = "Update or toggle tenant feature")
    public ResponseEntity<TenantFeature> updateFeature(
            @PathVariable("tenantId") UUID tenantId,
            @PathVariable("featureCode") String featureCode,
            @RequestBody UpdateFeatureRequest request) {
        return ResponseEntity.ok(tenantService.updateFeature(tenantId, featureCode, request));
    }

    @GetMapping("/{tenantId}/settings")
    @Operation(summary = "Get tenant settings")
    public ResponseEntity<List<TenantSetting>> getTenantSettings(@PathVariable("tenantId") UUID tenantId) {
        return ResponseEntity.ok(tenantService.getTenantSettings(tenantId));
    }

    @PutMapping("/{tenantId}/settings/{settingKey}")
    @Operation(summary = "Update tenant setting")
    public ResponseEntity<TenantSetting> updateSetting(
            @PathVariable("tenantId") UUID tenantId,
            @PathVariable("settingKey") String settingKey,
            @RequestBody UpdateSettingRequest request) {
        return ResponseEntity.ok(tenantService.updateSetting(tenantId, settingKey, request));
    }
}
