package com.auramarket.seller.api.controller;

import com.auramarket.seller.api.dto.ApiResponse;
import com.auramarket.seller.api.dto.PageResponse;
import com.auramarket.seller.api.dto.SellerDto;
import com.auramarket.seller.application.service.SellerService;
import com.auramarket.seller.domain.model.SellerStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/sellers")
@RequiredArgsConstructor
@Tag(name = "Admin Seller Moderation API", description = "Back-office seller application approval and suspension")
@CrossOrigin(origins = "*")
public class AdminSellerController {

    private final SellerService sellerService;

    @GetMapping
    @Operation(summary = "List All Sellers", description = "Retrieves paginated sellers for a tenant with optional status filter")
    public ResponseEntity<ApiResponse<PageResponse<SellerDto>>> getAllSellers(
            @RequestParam String tenantId,
            @RequestParam(required = false) SellerStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<SellerDto> response = sellerService.getAllSellers(tenantId, status, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Sellers retrieved successfully"));
    }

    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve Seller", description = "Approves a pending seller application")
    public ResponseEntity<ApiResponse<SellerDto>> approveSeller(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        SellerDto response = sellerService.approveSeller(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller approved successfully"));
    }

    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject Seller", description = "Rejects a pending seller application with reason")
    public ResponseEntity<ApiResponse<SellerDto>> rejectSeller(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false, defaultValue = "Documentation incomplete") String reason) {

        SellerDto response = sellerService.rejectSeller(id, tenantId, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller rejected successfully"));
    }

    @PutMapping("/{id}/suspend")
    @Operation(summary = "Suspend Seller", description = "Suspends an active seller account with reason")
    public ResponseEntity<ApiResponse<SellerDto>> suspendSeller(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false, defaultValue = "Policy violation") String reason) {

        SellerDto response = sellerService.suspendSeller(id, tenantId, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller suspended successfully"));
    }
}
