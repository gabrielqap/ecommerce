package com.gabriel.ecommerce.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.repository.OrderSummaryRepository;

@Service
public class OrderCreatedConsumer {

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @KafkaListener(topics = "order.created", groupId = "order-sync-group")
    public void syncOrderCreated(Order event, Double totalAmount) {
        OrderSummary orderSummary = new OrderSummary();
        orderSummary.setId(event.getId());
        orderSummary.setUserId(event.getUserId());
        orderSummary.setTotalAmount(totalAmount);
        orderSummary.setCreatedAt(event.getCreatedAt());
        orderSummary.setPaid(false);

        orderSummaryRepository.save(orderSummary);
    }

}