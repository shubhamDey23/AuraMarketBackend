package com.auramarket.order.api.dto;

import com.auramarket.order.domain.model.FulfillmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFulfillmentStatusRequest {

    @NotNull(message = "status is required")
    private FulfillmentStatus status;

    private String trackingNumber;
    private String carrier;
    private String shippingMethod;
}
