package com.auramarket.order.api.dto;

import com.auramarket.order.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private UUID id;
    private String tenantId;
    private UUID customerId;
    private String orderNumber;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private BigDecimal taxAmount;
    private BigDecimal shippingAmount;
    private BigDecimal discountAmount;
    private String currency;
    private String shippingAddressJson;
    private String billingAddressJson;
    private List<FulfillmentGroupDto> fulfillmentGroups;
    private List<OrderItemDto> items;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
