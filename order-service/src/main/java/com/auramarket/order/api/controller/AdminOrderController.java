package com.auramarket.order.api.controller;

import com.auramarket.order.api.dto.ApiResponse;
import com.auramarket.order.api.dto.OrderDto;
import com.auramarket.order.api.dto.PageResponse;
import com.auramarket.order.application.service.OrderService;
import com.auramarket.order.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
@Tag(name = "Admin Order API", description = "Platform-wide order oversight and moderation")
@CrossOrigin(origins = "*")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "Admin List All Orders", description = "Retrieves paginated orders across all tenants and sellers")
    public ResponseEntity<ApiResponse<PageResponse<OrderDto>>> getAllOrders(
            @RequestParam String tenantId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<OrderDto> response = orderService.getAllOrders(tenantId, status, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Admin orders retrieved successfully"));
    }
}
