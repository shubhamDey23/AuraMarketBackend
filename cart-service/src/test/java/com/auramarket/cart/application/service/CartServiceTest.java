package com.auramarket.cart.application.service;

import com.auramarket.cart.api.dto.AddToCartRequest;
import com.auramarket.cart.api.dto.CartDto;
import com.auramarket.cart.domain.model.Cart;
import com.auramarket.cart.domain.model.CartItem;
import com.auramarket.cart.domain.repository.CartItemRepository;
import com.auramarket.cart.domain.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private CartCacheService cacheService;

    @InjectMocks
    private CartService cartService;

    private UUID customerId;
    private UUID productId;
    private UUID sellerId;
    private Cart sampleCart;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        productId = UUID.randomUUID();
        sellerId = UUID.randomUUID();

        sampleCart = Cart.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .customerId(customerId)
                .items(new ArrayList<>())
                .build();
    }

    @Test
    void getCart_CreatesCartIfNotExist() {
        when(cacheService.getCart("AURA_AYURVEDA", customerId)).thenReturn(null);
        when(cartRepository.findByTenantIdAndCustomerId("AURA_AYURVEDA", customerId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(sampleCart);

        CartDto dto = cartService.getCart("AURA_AYURVEDA", customerId);

        assertNotNull(dto);
        assertEquals(customerId, dto.getCustomerId());
        assertEquals(BigDecimal.ZERO, dto.getTotalAmount());
    }

    @Test
    void addItemToCart_Success() {
        AddToCartRequest request = AddToCartRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .customerId(customerId)
                .productId(productId)
                .sellerId(sellerId)
                .productName("Organic Triphala")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(299.00))
                .build();

        when(cartRepository.findByTenantIdAndCustomerId("AURA_AYURVEDA", customerId)).thenReturn(Optional.of(sampleCart));
        when(cartItemRepository.findByCartIdAndProductId(sampleCart.getId(), productId)).thenReturn(Optional.empty());

        CartDto dto = cartService.addItemToCart(request);

        assertNotNull(dto);
        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
        verify(cacheService).evictCart("AURA_AYURVEDA", customerId);
    }
}
