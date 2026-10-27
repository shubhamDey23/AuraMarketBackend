package com.auramarket.payment.provider;

import com.auramarket.payment.domain.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResult {
    private boolean success;
    private String transactionReference;
    private PaymentStatus status;
    private String gatewayResponseJson;
    private String errorMessage;
}
