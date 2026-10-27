package com.auramarket.order.application.service;

import com.auramarket.order.api.dto.*;
import com.auramarket.order.domain.model.*;
import com.auramarket.order.domain.repository.FulfillmentGroupRepository;
import com.auramarket.order.domain.repository.OrderRepository;
import com.auramarket.order.domain.repository.OrderStatusHistoryRepository;
import com.auramarket.order.exception.OrderException;
import com.auramarket.order.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final FulfillmentGroupRepository fulfillmentGroupRepository;
    private final OrderStatusHistoryRepository statusHistoryRepository;
    private final OrderEventPublisherService eventPublisher;

    @Transactional
    public OrderDto createOrder(CreateOrderRequest request) {
        String orderNumber = "ORD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 4).toUpperCase();

        Order order = Order.builder()
                .tenantId(request.getTenantId())
                .customerId(request.getCustomerId())
                .orderNumber(orderNumber)
                .status(OrderStatus.PENDING)
                .shippingAddressJson(request.getShippingAddressJson())
                .billingAddressJson(request.getBillingAddressJson())
                .fulfillmentGroups(new ArrayList<>())
                .items(new ArrayList<>())
                .totalAmount(BigDecimal.ZERO)
                .build();

        // Group items by sellerId for fulfillment groups
        Map<UUID, List<OrderItemRequest>> itemsBySeller = request.getItems().stream()
                .collect(Collectors.groupingBy(OrderItemRequest::getSellerId));

        BigDecimal calculatedTotal = BigDecimal.ZERO;

        for (Map.Entry<UUID, List<OrderItemRequest>> entry : itemsBySeller.entrySet()) {
            UUID sellerId = entry.getKey();
            List<OrderItemRequest> sellerItems = entry.getValue();

            FulfillmentGroup fg = FulfillmentGroup.builder()
                    .order(order)
                    .sellerId(sellerId)
                    .status(FulfillmentStatus.PENDING)
                    .build();

            order.getFulfillmentGroups().add(fg);

            for (OrderItemRequest itemReq : sellerItems) {
                BigDecimal subtotal = itemReq.getUnitPrice().multiply(BigDecimal.valueOf(itemReq.getQuantity()));
                calculatedTotal = calculatedTotal.add(subtotal);

                OrderItem item = OrderItem.builder()
                        .order(order)
                        .productId(itemReq.getProductId())
                        .sellerId(sellerId)
                        .productName(itemReq.getProductName())
                        .sku(itemReq.getSku())
                        .quantity(itemReq.getQuantity())
                        .unitPrice(itemReq.getUnitPrice())
                        .subtotal(subtotal)
                        .build();

                order.getItems().add(item);
            }
        }

        order.setTotalAmount(calculatedTotal);
        Order savedOrder = orderRepository.save(order);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(savedOrder.getId())
                .oldStatus(null)
                .newStatus(OrderStatus.PENDING)
                .notes("Order created by customer")
                .build();
        statusHistoryRepository.save(history);

        OrderDto dto = mapToOrderDto(savedOrder);
        eventPublisher.publishEvent("order.created.v1", savedOrder.getTenantId(), dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getCustomerOrders(String tenantId, UUID customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage = orderRepository.findByTenantIdAndCustomerId(tenantId, customerId, pageable);
        return PageResponse.fromPage(orderPage.map(this::mapToOrderDto));
    }

    @Transactional(readOnly = true)
    public OrderDto getOrderById(UUID id, String tenantId, UUID customerId) {
        Order order = orderRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (customerId != null && !order.getCustomerId().equals(customerId)) {
            throw new OrderException("Customer not authorized to view this order", "UNAUTHORIZED_ORDER_ACCESS");
        }

        return mapToOrderDto(order);
    }

    @Transactional
    public OrderDto cancelOrder(UUID id, String tenantId, UUID customerId, String reason) {
        Order order = orderRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (customerId != null && !order.getCustomerId().equals(customerId)) {
            throw new OrderException("Customer not authorized to cancel this order", "UNAUTHORIZED_ORDER_ACCESS");
        }

        if (order.getStatus() == OrderStatus.SHIPPED || order.getStatus() == OrderStatus.DELIVERED) {
            throw new OrderException("Order cannot be cancelled in status: " + order.getStatus(), "INVALID_ORDER_STATE");
        }

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(savedOrder.getId())
                .oldStatus(oldStatus)
                .newStatus(OrderStatus.CANCELLED)
                .notes(reason != null ? reason : "Order cancelled by customer")
                .build();
        statusHistoryRepository.save(history);

        OrderDto dto = mapToOrderDto(savedOrder);
        eventPublisher.publishEvent("order.cancelled.v1", tenantId, dto);
        return dto;
    }

    @Transactional
    public OrderDto requestReturn(UUID id, String tenantId, UUID customerId, String reason) {
        Order order = orderRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        if (customerId != null && !order.getCustomerId().equals(customerId)) {
            throw new OrderException("Customer not authorized for this order", "UNAUTHORIZED_ORDER_ACCESS");
        }

        if (order.getStatus() != OrderStatus.DELIVERED) {
            throw new OrderException("Return can only be requested for DELIVERED orders", "INVALID_ORDER_STATE");
        }

        OrderStatus oldStatus = order.getStatus();
        order.setStatus(OrderStatus.RETURN_REQUESTED);
        Order savedOrder = orderRepository.save(order);

        OrderStatusHistory history = OrderStatusHistory.builder()
                .orderId(savedOrder.getId())
                .oldStatus(oldStatus)
                .newStatus(OrderStatus.RETURN_REQUESTED)
                .notes(reason != null ? reason : "Return requested by customer")
                .build();
        statusHistoryRepository.save(history);

        OrderDto dto = mapToOrderDto(savedOrder);
        eventPublisher.publishEvent("order.return-requested.v1", tenantId, dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<FulfillmentGroupDto> getSellerFulfillmentGroups(UUID sellerId, FulfillmentStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FulfillmentGroup> fgPage;
        if (status != null) {
            fgPage = fulfillmentGroupRepository.findBySellerIdAndStatus(sellerId, status, pageable);
        } else {
            fgPage = fulfillmentGroupRepository.findBySellerId(sellerId, pageable);
        }
        return PageResponse.fromPage(fgPage.map(this::mapToFulfillmentGroupDto));
    }

    @Transactional
    public FulfillmentGroupDto updateFulfillmentStatus(UUID fulfillmentGroupId, UUID sellerId, UpdateFulfillmentStatusRequest request) {
        FulfillmentGroup fg = fulfillmentGroupRepository.findByIdAndSellerId(fulfillmentGroupId, sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Fulfillment group not found for seller"));

        fg.setStatus(request.getStatus());
        if (request.getTrackingNumber() != null) fg.setTrackingNumber(request.getTrackingNumber());
        if (request.getCarrier() != null) fg.setCarrier(request.getCarrier());
        if (request.getShippingMethod() != null) fg.setShippingMethod(request.getShippingMethod());

        FulfillmentGroup saved = fulfillmentGroupRepository.save(fg);
        FulfillmentGroupDto dto = mapToFulfillmentGroupDto(saved);

        if (request.getStatus() == FulfillmentStatus.SHIPPED) {
            eventPublisher.publishEvent("order.shipped.v1", fg.getOrder().getTenantId(), dto);
        } else if (request.getStatus() == FulfillmentStatus.DELIVERED) {
            eventPublisher.publishEvent("order.delivered.v1", fg.getOrder().getTenantId(), dto);
        }

        return dto;
    }

    @Transactional(readOnly = true)
    public PageResponse<OrderDto> getAllOrders(String tenantId, OrderStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage;
        if (status != null) {
            orderPage = orderRepository.findByTenantIdAndStatus(tenantId, status, pageable);
        } else {
            orderPage = orderRepository.findByTenantId(tenantId, pageable);
        }
        return PageResponse.fromPage(orderPage.map(this::mapToOrderDto));
    }

    private OrderDto mapToOrderDto(Order order) {
        List<FulfillmentGroupDto> fgDtos = order.getFulfillmentGroups() != null ? order.getFulfillmentGroups().stream()
                .map(this::mapToFulfillmentGroupDto)
                .collect(Collectors.toList()) : new ArrayList<>();

        List<OrderItemDto> itemDtos = order.getItems() != null ? order.getItems().stream()
                .map(this::mapToOrderItemDto)
                .collect(Collectors.toList()) : new ArrayList<>();

        return OrderDto.builder()
                .id(order.getId())
                .tenantId(order.getTenantId())
                .customerId(order.getCustomerId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .taxAmount(order.getTaxAmount())
                .shippingAmount(order.getShippingAmount())
                .discountAmount(order.getDiscountAmount())
                .currency(order.getCurrency())
                .shippingAddressJson(order.getShippingAddressJson())
                .billingAddressJson(order.getBillingAddressJson())
                .fulfillmentGroups(fgDtos)
                .items(itemDtos)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private FulfillmentGroupDto mapToFulfillmentGroupDto(FulfillmentGroup fg) {
        return FulfillmentGroupDto.builder()
                .id(fg.getId())
                .orderId(fg.getOrder() != null ? fg.getOrder().getId() : null)
                .sellerId(fg.getSellerId())
                .status(fg.getStatus())
                .shippingMethod(fg.getShippingMethod())
                .trackingNumber(fg.getTrackingNumber())
                .carrier(fg.getCarrier())
                .createdAt(fg.getCreatedAt())
                .updatedAt(fg.getUpdatedAt())
                .build();
    }

    private OrderItemDto mapToOrderItemDto(OrderItem item) {
        return OrderItemDto.builder()
                .id(item.getId())
                .orderId(item.getOrder() != null ? item.getOrder().getId() : null)
                .fulfillmentGroupId(item.getFulfillmentGroupId())
                .productId(item.getProductId())
                .sellerId(item.getSellerId())
                .productName(item.getProductName())
                .sku(item.getSku())
                .quantity(item.getQuantity())
                .unitPrice(item.getUnitPrice())
                .subtotal(item.getSubtotal())
                .build();
    }
}
