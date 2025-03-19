package com.gabriel.ecommerce.entity.dto;

import java.util.List;

public record OrderPaidEventDTO(
    String orderId,
    List<OrderItemDTO> items
) {
}