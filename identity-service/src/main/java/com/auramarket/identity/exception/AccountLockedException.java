package com.auramarket.identity.exception;

import org.springframework.http.HttpStatus;

public class AccountLockedException extends IdentityException {
    public AccountLockedException(String message) {
        super(message, "AUTH_ACCOUNT_LOCKED", HttpStatus.FORBIDDEN);
    }
}
