package com.auramarket.payment.application.service;

import com.auramarket.payment.api.dto.*;
import com.auramarket.payment.domain.model.*;
import com.auramarket.payment.domain.repository.PaymentRepository;
import com.auramarket.payment.domain.repository.PaymentTransactionRepository;
import com.auramarket.payment.domain.repository.RefundRepository;
import com.auramarket.payment.exception.PaymentException;
import com.auramarket.payment.exception.ResourceNotFoundException;
import com.auramarket.payment.provider.PaymentProvider;
import com.auramarket.payment.provider.PaymentResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentTransactionRepository transactionRepository;
    private final RefundRepository refundRepository;
    private final Map<String, PaymentProvider> paymentProviders;
    private final PaymentEventPublisherService eventPublisher;

    @Transactional
    public PaymentDto initiatePayment(InitiatePaymentRequest request) {
        String providerName = request.getProvider() != null ? request.getProvider() : "TEST_PROVIDER";

        Payment payment = Payment.builder()
                .tenantId(request.getTenantId())
                .orderId(request.getOrderId())
                .customerId(request.getCustomerId())
                .amount(request.getAmount())
                .currency(request.getCurrency() != null ? request.getCurrency() : "INR")
                .provider(providerName)
                .status(PaymentStatus.INITIATED)
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        PaymentProvider provider = getProvider(providerName);
        PaymentResult result = provider.initiatePayment(savedPayment);

        if (result.isSuccess()) {
            savedPayment.setTransactionReference(result.getTransactionReference());
            savedPayment.setStatus(result.getStatus());
        } else {
            savedPayment.setStatus(PaymentStatus.FAILED);
        }

        Payment updatedPayment = paymentRepository.save(savedPayment);

        PaymentTransaction tx = PaymentTransaction.builder()
                .paymentId(updatedPayment.getId())
                .gatewayTransactionId(result.getTransactionReference())
                .paymentMethod("ONLINE")
                .status(updatedPayment.getStatus())
                .rawResponseJson(result.getGatewayResponseJson())
                .build();
        transactionRepository.save(tx);

        PaymentDto dto = mapToPaymentDto(updatedPayment);

        if (updatedPayment.getStatus() == PaymentStatus.FAILED) {
            eventPublisher.publishEvent("payment.failed.v1", request.getTenantId(), dto);
        } else {
            eventPublisher.publishEvent("payment.initiated.v1", request.getTenantId(), dto);
        }

        return dto;
    }

    @Transactional
    public PaymentDto verifyPayment(UUID paymentId, VerifyPaymentRequest request) {
        Payment payment = paymentRepository.findByIdAndTenantId(paymentId, request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + paymentId));

        PaymentProvider provider = getProvider(payment.getProvider());
        PaymentResult result = provider.verifyPayment(payment, request.getGatewaySignature());

        if (result.isSuccess()) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        Payment updatedPayment = paymentRepository.save(payment);

        PaymentTransaction tx = PaymentTransaction.builder()
                .paymentId(updatedPayment.getId())
                .gatewayTransactionId(request.getGatewayPaymentId())
                .paymentMethod("ONLINE")
                .status(updatedPayment.getStatus())
                .rawResponseJson(result.getGatewayResponseJson())
                .build();
        transactionRepository.save(tx);

        PaymentDto dto = mapToPaymentDto(updatedPayment);

        if (updatedPayment.getStatus() == PaymentStatus.SUCCESS) {
            eventPublisher.publishEvent("payment.completed.v1", request.getTenantId(), dto);
        } else {
            eventPublisher.publishEvent("payment.failed.v1", request.getTenantId(), dto);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(UUID paymentId, String tenantId) {
        Payment payment = paymentRepository.findByIdAndTenantId(paymentId, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + paymentId));
        return mapToPaymentDto(payment);
    }

    @Transactional
    public RefundDto processRefund(UUID paymentId, RefundRequest request) {
        Payment payment = paymentRepository.findByIdAndTenantId(paymentId, request.getTenantId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment record not found with id: " + paymentId));

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new PaymentException("Cannot refund payment that is not in SUCCESS state", "INVALID_PAYMENT_STATE");
        }

        Refund refund = Refund.builder()
                .paymentId(payment.getId())
                .amount(request.getAmount())
                .reason(request.getReason())
                .status(RefundStatus.PENDING)
                .build();

        Refund savedRefund = refundRepository.save(refund);

        PaymentProvider provider = getProvider(payment.getProvider());
        PaymentResult result = provider.processRefund(payment, savedRefund);

        if (result.isSuccess()) {
            savedRefund.setStatus(RefundStatus.SUCCESS);
            savedRefund.setRefundReference(result.getTransactionReference());
            payment.setStatus(PaymentStatus.REFUNDED);
        } else {
            savedRefund.setStatus(RefundStatus.FAILED);
        }

        refundRepository.save(savedRefund);
        paymentRepository.save(payment);

        RefundDto dto = mapToRefundDto(savedRefund);
        eventPublisher.publishEvent("payment.refunded.v1", request.getTenantId(), dto);
        return dto;
    }

    private PaymentProvider getProvider(String providerName) {
        if ("TEST_PROVIDER".equalsIgnoreCase(providerName)) {
            return paymentProviders.get("testPaymentProvider");
        }
        PaymentProvider provider = paymentProviders.get(providerName);
        if (provider == null) {
            log.warn("Payment provider {} not registered, falling back to testPaymentProvider", providerName);
            return paymentProviders.get("testPaymentProvider");
        }
        return provider;
    }

    private PaymentDto mapToPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .tenantId(payment.getTenantId())
                .orderId(payment.getOrderId())
                .customerId(payment.getCustomerId())
                .provider(payment.getProvider())
                .transactionReference(payment.getTransactionReference())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .build();
    }

    private RefundDto mapToRefundDto(Refund refund) {
        return RefundDto.builder()
                .id(refund.getId())
                .paymentId(refund.getPaymentId())
                .refundReference(refund.getRefundReference())
                .amount(refund.getAmount())
                .reason(refund.getReason())
                .status(refund.getStatus())
                .createdAt(refund.getCreatedAt())
                .build();
    }
}
