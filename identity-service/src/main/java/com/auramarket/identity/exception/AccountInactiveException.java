package com.auramarket.identity.exception;

import org.springframework.http.HttpStatus;

public class AccountInactiveException extends IdentityException {
    public AccountInactiveException(String message) {
        super(message, "AUTH_ACCOUNT_INACTIVE", HttpStatus.FORBIDDEN);
    }
}
