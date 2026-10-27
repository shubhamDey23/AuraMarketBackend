package com.auramarket.inventory.api.dto;

import com.auramarket.inventory.domain.model.ReservationStatus;
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
public class ReservationDto {
    private UUID id;
    private String tenantId;
    private UUID productId;
    private UUID inventoryId;
    private Integer quantity;
    private ReservationStatus status;
    private OffsetDateTime expiresAt;
    private OffsetDateTime createdAt;
}
