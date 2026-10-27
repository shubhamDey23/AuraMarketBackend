package com.auramarket.inventory.api.controller;

import com.auramarket.inventory.api.dto.ApiResponse;
import com.auramarket.inventory.api.dto.InventoryDto;
import com.auramarket.inventory.api.dto.PageResponse;
import com.auramarket.inventory.application.service.InventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/inventory")
@RequiredArgsConstructor
@Tag(name = "Admin Inventory API", description = "Back-office inventory overview across all merchants")
@CrossOrigin(origins = "*")
public class AdminInventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @Operation(summary = "Admin Inventory Overview", description = "Retrieves paginated stock records for platform administration")
    public ResponseEntity<ApiResponse<PageResponse<InventoryDto>>> getAllInventory(
            @RequestParam String tenantId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        PageResponse<InventoryDto> response = inventoryService.getAllInventory(tenantId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response, "Admin inventory list retrieved successfully"));
    }
}
