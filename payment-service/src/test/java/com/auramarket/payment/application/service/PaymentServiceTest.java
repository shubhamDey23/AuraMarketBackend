package com.auramarket.payment.application.service;

import com.auramarket.payment.api.dto.InitiatePaymentRequest;
import com.auramarket.payment.api.dto.PaymentDto;
import com.auramarket.payment.domain.model.Payment;
import com.auramarket.payment.domain.model.PaymentStatus;
import com.auramarket.payment.domain.repository.PaymentRepository;
import com.auramarket.payment.domain.repository.PaymentTransactionRepository;
import com.auramarket.payment.domain.repository.RefundRepository;
import com.auramarket.payment.provider.PaymentProvider;
import com.auramarket.payment.provider.PaymentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentTransactionRepository transactionRepository;
    @Mock
    private RefundRepository refundRepository;
    @Mock
    private PaymentProvider testPaymentProvider;
    @Mock
    private PaymentEventPublisherService eventPublisher;

    @Spy
    private Map<String, PaymentProvider> paymentProviders = new HashMap<>();

    @InjectMocks
    private PaymentService paymentService;

    private UUID orderId;
    private UUID customerId;
    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        paymentProviders.put("testPaymentProvider", testPaymentProvider);

        orderId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        samplePayment = Payment.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .orderId(orderId)
                .customerId(customerId)
                .amount(BigDecimal.valueOf(598.00))
                .currency("INR")
                .provider("TEST_PROVIDER")
                .status(PaymentStatus.PENDING)
                .transactionReference("TEST-TXN-12345")
                .build();
    }

    @Test
    void initiatePayment_Success() {
        InitiatePaymentRequest request = InitiatePaymentRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .orderId(orderId)
                .customerId(customerId)
                .amount(BigDecimal.valueOf(598.00))
                .provider("TEST_PROVIDER")
                .build();

        PaymentResult result = PaymentResult.builder()
                .success(true)
                .transactionReference("TEST-TXN-12345")
                .status(PaymentStatus.PENDING)
                .gatewayResponseJson("{}")
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(samplePayment);
        when(testPaymentProvider.initiatePayment(any(Payment.class))).thenReturn(result);

        PaymentDto dto = paymentService.initiatePayment(request);

        assertNotNull(dto);
        assertEquals(PaymentStatus.PENDING, dto.getStatus());
        verify(eventPublisher).publishEvent(eq("payment.initiated.v1"), eq("AURA_AYURVEDA"), any());
    }
}
