package com.auramarket.order.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotNull(message = "customerId is required")
    private UUID customerId;

    private String shippingAddressJson;
    private String billingAddressJson;

    @NotEmpty(message = "items cannot be empty")
    @Valid
    private List<OrderItemRequest> items;
}
