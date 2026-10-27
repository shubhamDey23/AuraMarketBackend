package com.auramarket.cart.api.controller;

import com.auramarket.cart.api.dto.*;
import com.auramarket.cart.application.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart API", description = "Shopping cart & multi-seller cart management")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartService cartService;

    @GetMapping
    @Operation(summary = "Get Customer Cart", description = "Retrieves active shopping cart for authenticated customer")
    public ResponseEntity<ApiResponse<CartDto>> getCart(
            @RequestParam String tenantId,
            @RequestParam UUID customerId) {

        CartDto response = cartService.getCart(tenantId, customerId);
        return ResponseEntity.ok(ApiResponse.success(response, "Cart retrieved successfully"));
    }

    @PostMapping("/items")
    @Operation(summary = "Add Item to Cart", description = "Adds a product line item to cart")
    public ResponseEntity<ApiResponse<CartDto>> addItem(@Valid @RequestBody AddToCartRequest request) {
        CartDto response = cartService.addItemToCart(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Item added to cart successfully"));
    }

    @PutMapping("/items/{itemId}")
    @Operation(summary = "Update Cart Item Quantity", description = "Updates quantity of a line item in cart")
    public ResponseEntity<ApiResponse<CartDto>> updateItemQuantity(
            @PathVariable UUID itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {

        CartDto response = cartService.updateItemQuantity(itemId, request);
        return ResponseEntity.ok(ApiResponse.success(response, "Cart item updated successfully"));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "Remove Item from Cart", description = "Removes a line item from cart")
    public ResponseEntity<ApiResponse<CartDto>> removeItem(
            @PathVariable UUID itemId,
            @RequestParam String tenantId,
            @RequestParam UUID customerId) {

        CartDto response = cartService.removeItemFromCart(itemId, tenantId, customerId);
        return ResponseEntity.ok(ApiResponse.success(response, "Cart item removed successfully"));
    }

    @DeleteMapping
    @Operation(summary = "Clear Cart", description = "Removes all items from customer cart")
    public ResponseEntity<ApiResponse<CartDto>> clearCart(
            @RequestParam String tenantId,
            @RequestParam UUID customerId) {

        CartDto response = cartService.clearCart(tenantId, customerId);
        return ResponseEntity.ok(ApiResponse.success(response, "Cart cleared successfully"));
    }
}
