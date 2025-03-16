package com.gabriel.ecommerce.service;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.messaging.OrderEventProducer;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderEventService {

    @Autowired
    private OrderEventProducer orderEventProducer;

    public void publishOrderCreatedEvent(Order order, Double totalAmount) {
        OrderCreatedEventDTO event = new OrderCreatedEventDTO(
                order.getId(),
                order.getUserId(),
                order.getItems(),
                totalAmount
        );
        orderEventProducer.sendOrderCreatedEvent(event);
    }
}