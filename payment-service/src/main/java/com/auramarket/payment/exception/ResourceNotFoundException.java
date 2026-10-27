package com.auramarket.payment.exception;

public class ResourceNotFoundException extends PaymentException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
