package com.gabriel.ecommerce.service;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.messaging.OrderEventProducer;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
                totalAmount,
                convertLocalDateTimeToSqlDate(order.getCreatedAt())
        );
        orderEventProducer.sendOrderCreatedEvent(event);
    }

    public static Date convertLocalDateTimeToSqlDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        
        LocalDate localDate = localDateTime.toLocalDate();
        return Date.valueOf(localDate);
    }
}