package com.auramarket.payment.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InitiatePaymentRequest {

    @NotBlank(message = "tenantId is required")
    private String tenantId;

    @NotNull(message = "orderId is required")
    private UUID orderId;

    @NotNull(message = "customerId is required")
    private UUID customerId;

    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "amount must be greater than 0")
    private BigDecimal amount;

    @Builder.Default
    private String currency = "INR";

    @Builder.Default
    private String provider = "TEST_PROVIDER";
}
