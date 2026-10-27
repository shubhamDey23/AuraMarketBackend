package com.auramarket.order.api.dto;

import com.auramarket.order.domain.model.FulfillmentStatus;
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
public class FulfillmentGroupDto {
    private UUID id;
    private UUID orderId;
    private UUID sellerId;
    private FulfillmentStatus status;
    private String shippingMethod;
    private String trackingNumber;
    private String carrier;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
