package com.auramarket.seller.exception;

public class DuplicateResourceException extends SellerException {
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
}
