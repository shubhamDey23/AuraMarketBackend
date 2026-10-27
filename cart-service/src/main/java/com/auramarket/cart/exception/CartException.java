package com.auramarket.cart.exception;

import lombok.Getter;

@Getter
public class CartException extends RuntimeException {
    private final String errorCode;

    public CartException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
