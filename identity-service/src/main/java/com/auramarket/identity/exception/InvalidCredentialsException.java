package com.auramarket.identity.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends IdentityException {
    public InvalidCredentialsException(String message) {
        super(message, "AUTH_INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED);
    }
}
