package com.auramarket.catalog.exception;

public class ResourceNotFoundException extends CatalogException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
