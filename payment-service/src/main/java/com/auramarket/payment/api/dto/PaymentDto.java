package com.auramarket.payment.api.dto;

import com.auramarket.payment.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private UUID id;
    private String tenantId;
    private UUID orderId;
    private UUID customerId;
    private String provider;
    private String transactionReference;
    private BigDecimal amount;
    private String currency;
    private PaymentStatus status;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
