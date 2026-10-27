package com.auramarket.identity.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends IdentityException {
    public EmailAlreadyExistsException(String message) {
        super(message, "AUTH_EMAIL_ALREADY_EXISTS", HttpStatus.CONFLICT);
    }
}
