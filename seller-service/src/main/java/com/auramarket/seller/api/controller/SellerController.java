package com.auramarket.seller.api.controller;

import com.auramarket.seller.api.dto.ApiResponse;
import com.auramarket.seller.api.dto.OnboardSellerRequest;
import com.auramarket.seller.api.dto.SellerDto;
import com.auramarket.seller.api.dto.UpdateSellerRequest;
import com.auramarket.seller.application.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
@Tag(name = "Seller Merchant API", description = "Merchant onboarding and profile management")
@CrossOrigin(origins = "*")
public class SellerController {

    private final SellerService sellerService;

    @PostMapping
    @Operation(summary = "Onboard Seller", description = "Submits seller onboarding application for platform review")
    public ResponseEntity<ApiResponse<SellerDto>> onboardSeller(@Valid @RequestBody OnboardSellerRequest request) {
        SellerDto response = sellerService.onboardSeller(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Seller onboarding application submitted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Seller by ID", description = "Retrieves seller store profile by ID")
    public ResponseEntity<ApiResponse<SellerDto>> getSellerById(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        SellerDto response = sellerService.getSellerById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller details retrieved successfully"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get My Seller Profile", description = "Retrieves seller profile for logged-in user")
    public ResponseEntity<ApiResponse<SellerDto>> getMySellerProfile(
            @RequestParam String tenantId,
            @RequestParam UUID userId) {

        SellerDto response = sellerService.getSellerByUserId(tenantId, userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller profile retrieved successfully"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Seller Profile", description = "Updates seller store profile and contact details")
    public ResponseEntity<ApiResponse<SellerDto>> updateSeller(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam UUID userId,
            @Valid @RequestBody UpdateSellerRequest request) {

        SellerDto response = sellerService.updateSeller(id, tenantId, userId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller profile updated successfully"));
    }
}
