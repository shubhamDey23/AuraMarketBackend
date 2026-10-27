package com.auramarket.payment.provider;

import com.auramarket.payment.domain.model.Payment;
import com.auramarket.payment.domain.model.Refund;

public interface PaymentProvider {
    PaymentResult initiatePayment(Payment payment);
    PaymentResult verifyPayment(Payment payment, String gatewaySignature);
    PaymentResult processRefund(Payment payment, Refund refund);
}
