package com.auramarket.cart.application.service;

import com.auramarket.cart.api.dto.*;
import com.auramarket.cart.domain.model.Cart;
import com.auramarket.cart.domain.model.CartItem;
import com.auramarket.cart.domain.repository.CartItemRepository;
import com.auramarket.cart.domain.repository.CartRepository;
import com.auramarket.cart.exception.CartException;
import com.auramarket.cart.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartCacheService cacheService;

    @Transactional
    public CartDto getCart(String tenantId, UUID customerId) {
        CartDto cached = cacheService.getCart(tenantId, customerId);
        if (cached != null) return cached;

        Cart cart = getOrCreateCart(tenantId, customerId);
        CartDto dto = mapToCartDto(cart);

        cacheService.putCart(tenantId, customerId, dto);
        return dto;
    }

    @Transactional
    public CartDto addItemToCart(AddToCartRequest request) {
        Cart cart = getOrCreateCart(request.getTenantId(), request.getCustomerId());

        Optional<CartItem> existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId());
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            item.calculateSubtotal();
            cartItemRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .productId(request.getProductId())
                    .sellerId(request.getSellerId())
                    .productName(request.getProductName())
                    .sku(request.getSku())
                    .quantity(request.getQuantity())
                    .unitPrice(request.getUnitPrice())
                    .build();

            cart.getItems().add(item);
            cartRepository.save(cart);
        }

        cacheService.evictCart(request.getTenantId(), request.getCustomerId());
        return getCart(request.getTenantId(), request.getCustomerId());
    }

    @Transactional
    public CartDto updateItemQuantity(UUID itemId, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (!item.getCart().getCustomerId().equals(request.getCustomerId()) ||
                !item.getCart().getTenantId().equals(request.getTenantId())) {
            throw new CartException("Customer not authorized to modify this cart item", "UNAUTHORIZED_CART_ACCESS");
        }

        item.setQuantity(request.getQuantity());
        item.calculateSubtotal();
        cartItemRepository.save(item);

        cacheService.evictCart(request.getTenantId(), request.getCustomerId());
        return getCart(request.getTenantId(), request.getCustomerId());
    }

    @Transactional
    public CartDto removeItemFromCart(UUID itemId, String tenantId, UUID customerId) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + itemId));

        if (!item.getCart().getCustomerId().equals(customerId) ||
                !item.getCart().getTenantId().equals(tenantId)) {
            throw new CartException("Customer not authorized to remove this cart item", "UNAUTHORIZED_CART_ACCESS");
        }

        Cart cart = item.getCart();
        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        cacheService.evictCart(tenantId, customerId);
        return getCart(tenantId, customerId);
    }

    @Transactional
    public CartDto clearCart(String tenantId, UUID customerId) {
        Optional<Cart> optionalCart = cartRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        if (optionalCart.isPresent()) {
            Cart cart = optionalCart.get();
            cart.getItems().clear();
            cartRepository.save(cart);
        }

        cacheService.evictCart(tenantId, customerId);
        return getCart(tenantId, customerId);
    }

    private Cart getOrCreateCart(String tenantId, UUID customerId) {
        return cartRepository.findByTenantIdAndCustomerId(tenantId, customerId)
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .tenantId(tenantId)
                        .customerId(customerId)
                        .items(new ArrayList<>())
                        .build()));
    }

    private CartDto mapToCartDto(Cart cart) {
        List<CartItemDto> itemDtos = cart.getItems() != null ? cart.getItems().stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList()) : new ArrayList<>();

        BigDecimal totalAmount = itemDtos.stream()
                .map(item -> item.getSubtotal() != null ? item.getSubtotal() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = itemDtos.stream()
                .mapToInt(CartItemDto::getQuantity)
                .sum();

        return CartDto.builder()
                .id(cart.getId())
                .tenantId(cart.getTenantId())
                .customerId(cart.getCustomerId())
                .items(itemDtos)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .build();
    }

    private CartItemDto mapToCartItemDto(CartItem item) {
        BigDecimal subtotal = item.getSubtotal();
        if (subtotal == null && item.getUnitPrice() != null && item.getQuantity() != null) {
            subtotal = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
        }
        return CartItemDto.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .sellerId(item.getSellerId())
                .productName(item.getProductName())
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(subtotal != null ? subtotal : BigDecimal.ZERO)
                .build();
    }
}
