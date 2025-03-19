package com.gabriel.ecommerce.entity.dto;

import java.sql.Date;
import java.util.List;

public record OrderCreatedEventDTO (String orderId, String userId, List<OrderItemDTO> items, Double totalAmount, Date createdAt) {
}