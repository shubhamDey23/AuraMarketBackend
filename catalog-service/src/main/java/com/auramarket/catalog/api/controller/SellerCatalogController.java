package com.auramarket.catalog.api.controller;

import com.auramarket.catalog.api.dto.ApiResponse;
import com.auramarket.catalog.api.dto.CreateProductRequest;
import com.auramarket.catalog.api.dto.ProductDto;
import com.auramarket.catalog.api.dto.UpdateProductRequest;
import com.auramarket.catalog.application.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller/products")
@RequiredArgsConstructor
@Tag(name = "Seller Catalog API", description = "Merchant product management endpoints")
@CrossOrigin(origins = "*")
public class SellerCatalogController {

    private final CatalogService catalogService;

    @PostMapping
    @Operation(summary = "Create Product", description = "Creates a new product listing pending moderation")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody CreateProductRequest request) {
        ProductDto response = catalogService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Product created successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Product", description = "Updates an existing product listing")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam UUID sellerId,
            @Valid @RequestBody UpdateProductRequest request) {

        ProductDto response = catalogService.updateProduct(id, tenantId, sellerId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Product", description = "Soft deletes/archives a product listing")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam UUID sellerId) {

        catalogService.deleteProduct(id, tenantId, sellerId);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
}
