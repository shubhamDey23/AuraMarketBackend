package com.auramarket.order.application.service;

import com.auramarket.order.api.dto.CreateOrderRequest;
import com.auramarket.order.api.dto.OrderDto;
import com.auramarket.order.api.dto.OrderItemRequest;
import com.auramarket.order.domain.model.Order;
import com.auramarket.order.domain.model.OrderStatus;
import com.auramarket.order.domain.model.OrderStatusHistory;
import com.auramarket.order.domain.repository.FulfillmentGroupRepository;
import com.auramarket.order.domain.repository.OrderRepository;
import com.auramarket.order.domain.repository.OrderStatusHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private FulfillmentGroupRepository fulfillmentGroupRepository;
    @Mock
    private OrderStatusHistoryRepository statusHistoryRepository;
    @Mock
    private OrderEventPublisherService eventPublisher;

    @InjectMocks
    private OrderService orderService;

    private UUID customerId;
    private UUID sellerId;
    private UUID productId;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        sellerId = UUID.randomUUID();
        productId = UUID.randomUUID();

        sampleOrder = Order.builder()
                .id(UUID.randomUUID())
                .tenantId("AURA_AYURVEDA")
                .customerId(customerId)
                .orderNumber("ORD-12345")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.valueOf(598.00))
                .fulfillmentGroups(new ArrayList<>())
                .items(new ArrayList<>())
                .build();
    }

    @Test
    void createOrder_Success() {
        OrderItemRequest itemReq = OrderItemRequest.builder()
                .productId(productId)
                .sellerId(sellerId)
                .productName("Organic Triphala")
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(299.00))
                .build();

        CreateOrderRequest request = CreateOrderRequest.builder()
                .tenantId("AURA_AYURVEDA")
                .customerId(customerId)
                .items(List.of(itemReq))
                .build();

        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderDto dto = orderService.createOrder(request);

        assertNotNull(dto);
        assertEquals(OrderStatus.PENDING, dto.getStatus());
        verify(statusHistoryRepository).save(any(OrderStatusHistory.class));
        verify(eventPublisher).publishEvent(eq("order.created.v1"), eq("AURA_AYURVEDA"), any());
    }

    @Test
    void cancelOrder_Success() {
        when(orderRepository.findByIdAndTenantId(sampleOrder.getId(), "AURA_AYURVEDA")).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderDto dto = orderService.cancelOrder(sampleOrder.getId(), "AURA_AYURVEDA", customerId, "User cancelled");

        assertNotNull(dto);
        assertEquals(OrderStatus.CANCELLED, dto.getStatus());
        verify(eventPublisher).publishEvent(eq("order.cancelled.v1"), eq("AURA_AYURVEDA"), any());
    }
}
