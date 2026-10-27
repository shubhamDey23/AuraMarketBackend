package com.auramarket.inventory.api.dto;

import com.auramarket.inventory.domain.model.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto {
    private UUID id;
    private String tenantId;
    private UUID productId;
    private UUID sellerId;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private Integer damagedQuantity;
    private Integer reorderLevel;
    private InventoryStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
