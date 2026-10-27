package com.auramarket.inventory.api.controller;

import com.auramarket.inventory.api.dto.*;
import com.auramarket.inventory.application.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory API", description = "Real-time stock availability, adjustments, and reservations")
@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/products/{productId}")
    @Operation(summary = "Get Product Inventory", description = "Retrieves stock availability for a specific product")
    public ResponseEntity<ApiResponse<InventoryDto>> getInventoryByProductId(
            @PathVariable UUID productId,
            @RequestParam String tenantId) {

        InventoryDto response = inventoryService.getInventoryByProductId(tenantId, productId);
        return ResponseEntity.ok(ApiResponse.success(response, "Inventory details retrieved successfully"));
    }

    @GetMapping("/seller/me")
    @Operation(summary = "Get Seller Inventory List", description = "Retrieves paginated stock records for logged in merchant")
    public ResponseEntity<ApiResponse<PageResponse<InventoryDto>>> getSellerInventory(
            @RequestParam String tenantId,
            @RequestParam UUID sellerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<InventoryDto> response = inventoryService.getSellerInventory(tenantId, sellerId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller inventory retrieved successfully"));
    }

    @PostMapping("/adjustments")
    @Operation(summary = "Stock Adjustment", description = "Adds, removes, or marks stock as damaged")
    public ResponseEntity<ApiResponse<InventoryDto>> adjustStock(@Valid @RequestBody StockAdjustmentRequest request) {
        InventoryDto response = inventoryService.adjustStock(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock adjusted successfully"));
    }

    @PostMapping("/reservations")
    @Operation(summary = "Reserve Stock", description = "Creates temporary stock reservation for checkout flow")
    public ResponseEntity<ApiResponse<ReservationDto>> reserveStock(@Valid @RequestBody CreateReservationRequest request) {
        ReservationDto response = inventoryService.reserveStock(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Stock reserved successfully"));
    }

    @PostMapping("/reservations/{id}/confirm")
    @Operation(summary = "Confirm Reservation", description = "Confirms stock reservation upon successful order placement")
    public ResponseEntity<ApiResponse<ReservationDto>> confirmReservation(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        ReservationDto response = inventoryService.confirmReservation(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock reservation confirmed successfully"));
    }

    @PostMapping("/reservations/{id}/release")
    @Operation(summary = "Release Reservation", description = "Releases stock reservation upon cart expiration or order cancellation")
    public ResponseEntity<ApiResponse<ReservationDto>> releaseReservation(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        ReservationDto response = inventoryService.releaseReservation(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Stock reservation released successfully"));
    }

    @GetMapping("/transactions")
    @Operation(summary = "Get Inventory Transactions", description = "Retrieves stock audit trail for an inventory item")
    public ResponseEntity<ApiResponse<PageResponse<InventoryTransactionDto>>> getTransactions(
            @RequestParam UUID inventoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<InventoryTransactionDto> response = inventoryService.getInventoryTransactions(inventoryId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Transactions retrieved successfully"));
    }
}
