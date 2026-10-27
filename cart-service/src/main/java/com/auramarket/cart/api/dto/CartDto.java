package com.auramarket.cart.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDto {
    private UUID id;
    private String tenantId;
    private UUID customerId;
    private List<CartItemDto> items;
    private BigDecimal totalAmount;
    private Integer totalItems;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
