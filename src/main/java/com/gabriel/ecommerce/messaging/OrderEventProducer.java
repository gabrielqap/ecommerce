package com.gabriel.ecommerce.messaging;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.entity.dto.OrderPaidEventDTO;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEventDTO event) {
        kafkaTemplate.send("order.created", event);
        System.out.println("Sent order.created event: " + event);
    }

    public void sendOrderPaidEvent(OrderPaidEventDTO event) {
        kafkaTemplate.send("order.paid", event);
        System.out.println("Sent order.paid event: " + event);
    }
}