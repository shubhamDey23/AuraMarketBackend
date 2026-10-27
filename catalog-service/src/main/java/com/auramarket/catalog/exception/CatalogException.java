package com.auramarket.catalog.exception;

import lombok.Getter;

@Getter
public class CatalogException extends RuntimeException {
    private final String errorCode;

    public CatalogException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
