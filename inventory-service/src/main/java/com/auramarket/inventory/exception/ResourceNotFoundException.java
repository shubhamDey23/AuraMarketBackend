package com.auramarket.inventory.exception;

public class ResourceNotFoundException extends InventoryException {
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
}
