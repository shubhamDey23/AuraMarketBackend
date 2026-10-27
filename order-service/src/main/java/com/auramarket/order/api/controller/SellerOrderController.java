package com.auramarket.order.api.controller;

import com.auramarket.order.api.dto.ApiResponse;
import com.auramarket.order.api.dto.FulfillmentGroupDto;
import com.auramarket.order.api.dto.PageResponse;
import com.auramarket.order.api.dto.UpdateFulfillmentStatusRequest;
import com.auramarket.order.application.service.OrderService;
import com.auramarket.order.domain.model.FulfillmentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/seller/orders")
@RequiredArgsConstructor
@Tag(name = "Seller Order API", description = "Merchant order fulfillment and tracking updates")
@CrossOrigin(origins = "*")
public class SellerOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Get Seller Fulfillment Groups", description = "Retrieves paginated fulfillment tasks for a merchant")
    public ResponseEntity<ApiResponse<PageResponse<FulfillmentGroupDto>>> getSellerFulfillmentGroups(
            @RequestParam UUID sellerId,
            @RequestParam(required = false) FulfillmentStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<FulfillmentGroupDto> response = orderService.getSellerFulfillmentGroups(sellerId, status, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Seller fulfillment groups retrieved successfully"));
    }

    @PutMapping("/{fulfillmentGroupId}/status")
    @Operation(summary = "Update Fulfillment Status", description = "Updates tracking details and status for seller shipment")
    public ResponseEntity<ApiResponse<FulfillmentGroupDto>> updateFulfillmentStatus(
            @PathVariable UUID fulfillmentGroupId,
            @RequestParam UUID sellerId,
            @Valid @RequestBody UpdateFulfillmentStatusRequest request) {

        FulfillmentGroupDto response = orderService.updateFulfillmentStatus(fulfillmentGroupId, sellerId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Fulfillment status updated successfully"));
    }
}
