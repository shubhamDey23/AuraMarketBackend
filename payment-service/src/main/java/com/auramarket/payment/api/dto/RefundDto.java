package com.auramarket.payment.api.dto;

import com.auramarket.payment.domain.model.RefundStatus;
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
public class RefundDto {
    private UUID id;
    private UUID paymentId;
    private String refundReference;
    private BigDecimal amount;
    private String reason;
    private RefundStatus status;
    private OffsetDateTime createdAt;
}
