package com.auramarket.inventory.api.dto;

import com.auramarket.inventory.domain.model.TransactionType;
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
public class InventoryTransactionDto {
    private UUID id;
    private UUID inventoryId;
    private TransactionType transactionType;
    private Integer quantityChange;
    private String referenceId;
    private String notes;
    private OffsetDateTime createdAt;
}
