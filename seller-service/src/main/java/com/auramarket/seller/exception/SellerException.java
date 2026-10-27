package com.auramarket.seller.exception;

import lombok.Getter;

@Getter
public class SellerException extends RuntimeException {
    private final String errorCode;

    public SellerException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
