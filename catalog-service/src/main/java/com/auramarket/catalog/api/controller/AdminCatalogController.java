package com.auramarket.catalog.api.controller;

import com.auramarket.catalog.api.dto.ApiResponse;
import com.auramarket.catalog.api.dto.ProductDto;
import com.auramarket.catalog.application.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/catalog/products")
@RequiredArgsConstructor
@Tag(name = "Admin Catalog API", description = "Back-office moderation endpoints for product catalog")
@CrossOrigin(origins = "*")
public class AdminCatalogController {

    private final CatalogService catalogService;

    @GetMapping
    @Operation(summary = "List Tenant Products", description = "Lists products in all statuses for back-office moderation")
    public ResponseEntity<ApiResponse<com.auramarket.catalog.api.dto.PageResponse<ProductDto>>> getAdminProducts(
            @RequestParam String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        var response = catalogService.getAdminProducts(tenantId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Catalog products retrieved successfully"));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve Product", description = "Approves a pending product for storefront display")
    public ResponseEntity<ApiResponse<ProductDto>> approveProduct(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        ProductDto response = catalogService.approveProduct(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Product approved successfully"));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject Product", description = "Rejects a pending product listing with optional reason")
    public ResponseEntity<ApiResponse<ProductDto>> rejectProduct(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false, defaultValue = "Violates guidelines") String reason) {

        ProductDto response = catalogService.rejectProduct(id, tenantId, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Product rejected successfully"));
    }
}
