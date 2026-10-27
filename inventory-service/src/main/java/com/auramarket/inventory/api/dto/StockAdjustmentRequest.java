package com.auramarket.inventory.api.dto;

import com.auramarket.inventory.domain.model.TransactionType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotNull(message = "productId is required")
    private UUID productId;

    @NotNull(message = "sellerId is required")
    private UUID sellerId;

    @NotNull(message = "transactionType is required")
    private TransactionType transactionType;

    @NotNull(message = "quantityChange is required")
    @Min(value = 1, message = "quantityChange must be at least 1")
    private Integer quantityChange;

    private String referenceId;
    private String notes;
}
