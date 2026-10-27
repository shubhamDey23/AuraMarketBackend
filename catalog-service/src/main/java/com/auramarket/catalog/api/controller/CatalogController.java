package com.auramarket.catalog.api.controller;

import com.auramarket.catalog.api.dto.*;
import com.auramarket.catalog.application.service.CatalogService;
import com.auramarket.catalog.domain.model.ProductStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Tag(name = "Public Catalog API", description = "Browse products, categories, and brands")
@CrossOrigin(origins = "*")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/products")
    @Operation(summary = "Search & Filter Products", description = "Retrieves paginated products for a tenant")
    public ResponseEntity<ApiResponse<PageResponse<ProductDto>>> getProducts(
            @RequestParam String tenantId,
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID subcategoryId,
            @RequestParam(required = false) UUID brandId,
            @RequestParam(required = false) UUID sellerId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) ProductStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        PageResponse<ProductDto> response = catalogService.getProducts(tenantId, categoryId, subcategoryId, brandId,
                sellerId, minPrice, maxPrice, status, page, size, sortBy, sortDir);
        return ResponseEntity.ok(ApiResponse.success(response, "Products retrieved successfully"));
    }

    @GetMapping("/products/{id}")
    @Operation(summary = "Get Product by ID", description = "Retrieves product details by ID")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        ProductDto response = catalogService.getProductById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Product retrieved successfully"));
    }

    @GetMapping("/products/slug/{slug}")
    @Operation(summary = "Get Product by Slug", description = "Retrieves product details by slug")
    public ResponseEntity<ApiResponse<ProductDto>> getProductBySlug(
            @PathVariable String slug,
            @RequestParam String tenantId) {

        ProductDto response = catalogService.getProductBySlug(tenantId, slug);
        return ResponseEntity.ok(ApiResponse.success(response, "Product retrieved successfully"));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get Categories", description = "Retrieves active categories for a tenant")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategories(@RequestParam String tenantId) {
        List<CategoryDto> response = catalogService.getCategories(tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Categories retrieved successfully"));
    }

    @GetMapping("/categories/{id}")
    @Operation(summary = "Get Category by ID", description = "Retrieves category details by ID")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        CategoryDto response = catalogService.getCategoryById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Category retrieved successfully"));
    }

    @GetMapping("/brands")
    @Operation(summary = "Get Brands", description = "Retrieves active brands for a tenant")
    public ResponseEntity<ApiResponse<List<BrandDto>>> getBrands(@RequestParam String tenantId) {
        List<BrandDto> response = catalogService.getBrands(tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Brands retrieved successfully"));
    }
}
