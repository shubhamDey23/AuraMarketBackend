package com.auramarket.payment.api.controller;

import com.auramarket.payment.api.dto.*;
import com.auramarket.payment.application.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Gateway API", description = "Payment intent initiation, verification, and refund management")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initiate")
    @Operation(summary = "Initiate Payment", description = "Initiates a new payment transaction intent for checkout")
    public ResponseEntity<ApiResponse<PaymentDto>> initiatePayment(@Valid @RequestBody InitiatePaymentRequest request) {
        PaymentDto response = paymentService.initiatePayment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Payment initiated successfully"));
    }

    @PostMapping("/{id}/verify")
    @Operation(summary = "Verify Payment", description = "Verifies payment signature from gateway callback")
    public ResponseEntity<ApiResponse<PaymentDto>> verifyPayment(
            @PathVariable UUID id,
            @Valid @RequestBody VerifyPaymentRequest request) {

        PaymentDto response = paymentService.verifyPayment(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment verified successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Payment Details", description = "Retrieves payment details by ID")
    public ResponseEntity<ApiResponse<PaymentDto>> getPaymentById(
            @PathVariable UUID id,
            @RequestParam String tenantId) {

        PaymentDto response = paymentService.getPaymentById(id, tenantId);
        return ResponseEntity.ok(ApiResponse.success(response, "Payment details retrieved successfully"));
    }

    @PostMapping("/{id}/refund")
    @Operation(summary = "Process Refund", description = "Executes refund for a completed payment")
    public ResponseEntity<ApiResponse<RefundDto>> processRefund(
            @PathVariable UUID id,
            @Valid @RequestBody RefundRequest request) {

        RefundDto response = paymentService.processRefund(id, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Refund processed successfully"));
    }
}
