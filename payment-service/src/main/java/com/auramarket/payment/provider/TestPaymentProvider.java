package com.auramarket.payment.provider;

import com.auramarket.payment.domain.model.Payment;
import com.auramarket.payment.domain.model.PaymentStatus;
import com.auramarket.payment.domain.model.Refund;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("testPaymentProvider")
@Slf4j
public class TestPaymentProvider implements PaymentProvider {

    @Override
    public PaymentResult initiatePayment(Payment payment) {
        String txRef = "TEST-TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("TestPaymentProvider: Simulated payment intent created for order {} with ref {}", payment.getOrderId(), txRef);

        return PaymentResult.builder()
                .success(true)
                .transactionReference(txRef)
                .status(PaymentStatus.PENDING)
                .gatewayResponseJson("{\"provider\": \"TEST_PROVIDER\", \"status\": \"PENDING\", \"reference\": \"" + txRef + "\"}")
                .build();
    }

    @Override
    public PaymentResult verifyPayment(Payment payment, String gatewaySignature) {
        log.info("TestPaymentProvider: Verifying payment {} with signature {}", payment.getId(), gatewaySignature);

        return PaymentResult.builder()
                .success(true)
                .transactionReference(payment.getTransactionReference())
                .status(PaymentStatus.SUCCESS)
                .gatewayResponseJson("{\"provider\": \"TEST_PROVIDER\", \"status\": \"SUCCESS\", \"verified\": true}")
                .build();
    }

    @Override
    public PaymentResult processRefund(Payment payment, Refund refund) {
        String refundRef = "TEST-REFUND-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        log.info("TestPaymentProvider: Processing refund of {} for payment {}", refund.getAmount(), payment.getId());

        return PaymentResult.builder()
                .success(true)
                .transactionReference(refundRef)
                .status(PaymentStatus.REFUNDED)
                .gatewayResponseJson("{\"provider\": \"TEST_PROVIDER\", \"status\": \"REFUNDED\", \"refundRef\": \"" + refundRef + "\"}")
                .build();
    }
}
