package com.auramarket.catalog.exception;

public class DuplicateResourceException extends CatalogException {
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
}
