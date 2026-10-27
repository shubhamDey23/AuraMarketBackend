package com.auramarket.inventory.exception;

public class InsufficientStockException extends InventoryException {
    public InsufficientStockException(String message) {
        super(message, "INSUFFICIENT_STOCK");
    }
}
