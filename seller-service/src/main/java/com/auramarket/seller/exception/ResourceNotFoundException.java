package com.auramarket.seller.exception;

public class ResourceNotFoundException extends SellerException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
