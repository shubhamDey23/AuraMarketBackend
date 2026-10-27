package com.auramarket.order.api.controller;

import com.auramarket.order.api.dto.*;
import com.auramarket.order.application.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Customer Order API", description = "Order placement, tracking, and returns for shoppers")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create Order", description = "Places a new customer order with automatic multi-seller fulfillment groups")
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Order created successfully"));
    }

    @GetMapping
    @Operation(summary = "Get Customer Orders", description = "Retrieves paginated orders for a customer")
    public ResponseEntity<ApiResponse<PageResponse<OrderDto>>> getCustomerOrders(
            @RequestParam String tenantId,
            @RequestParam UUID customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<OrderDto> response = orderService.getCustomerOrders(tenantId, customerId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Customer orders retrieved successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Order Details", description = "Retrieves full details for a specific order")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderById(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false) UUID customerId) {

        OrderDto response = orderService.getOrderById(id, tenantId, customerId);
        return ResponseEntity.ok(ApiResponse.success(response, "Order details retrieved successfully"));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel Order", description = "Cancels an un-shipped customer order")
    public ResponseEntity<ApiResponse<OrderDto>> cancelOrder(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false, defaultValue = "Cancelled by user") String reason) {

        OrderDto response = orderService.cancelOrder(id, tenantId, customerId, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Order cancelled successfully"));
    }

    @PostMapping("/{id}/return")
    @Operation(summary = "Request Order Return", description = "Submits return request for delivered order")
    public ResponseEntity<ApiResponse<OrderDto>> requestReturn(
            @PathVariable UUID id,
            @RequestParam String tenantId,
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false, defaultValue = "Product return requested") String reason) {

        OrderDto response = orderService.requestReturn(id, tenantId, customerId, reason);
        return ResponseEntity.ok(ApiResponse.success(response, "Return requested successfully"));
    }
}
