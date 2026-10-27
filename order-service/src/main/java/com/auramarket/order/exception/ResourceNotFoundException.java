package com.auramarket.order.exception;

public class ResourceNotFoundException extends OrderException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
