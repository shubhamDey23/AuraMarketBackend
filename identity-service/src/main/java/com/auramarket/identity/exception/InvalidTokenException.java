package com.auramarket.identity.exception;

import org.springframework.http.HttpStatus;

public class InvalidTokenException extends IdentityException {
    public InvalidTokenException(String message, String errorCode) {
        super(message, errorCode, HttpStatus.UNAUTHORIZED);
    }

    public InvalidTokenException(String message) {
        super(message, "AUTH_INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED);
    }
}
