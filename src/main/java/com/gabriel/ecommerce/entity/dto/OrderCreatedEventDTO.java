package com.gabriel.ecommerce.entity.dto;

import java.util.List;

public record OrderCreatedEventDTO (String orderId, String userId, List<OrderItemDTO> items, Double totalAmount) {
}