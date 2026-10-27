package com.auramarket.cart.exception;

public class ResourceNotFoundException extends CartException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
