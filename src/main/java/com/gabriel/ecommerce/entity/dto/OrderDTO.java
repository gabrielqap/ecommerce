package com.gabriel.ecommerce.entity.dto;

import java.util.List;

public record OrderDTO(String userId, List<OrderItemDTO> items ){}