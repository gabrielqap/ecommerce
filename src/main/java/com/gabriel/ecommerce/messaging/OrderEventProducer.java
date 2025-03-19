package com.gabriel.ecommerce.messaging;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.entity.dto.OrderPaidEventDTO;

import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderEventProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(OrderCreatedEventDTO event) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order.created", messageJson);
            System.out.println("Sent order.created event: " + messageJson);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing event: " + e.getMessage());
        }
    }
    
    public void sendOrderPaidEvent(OrderPaidEventDTO event) {
        try  {
            ObjectMapper objectMapper = new ObjectMapper();
            String messageJson = objectMapper.writeValueAsString(event);
            kafkaTemplate.send("order.paid", messageJson);
            System.out.println("Sent order.paid event: " + event);
        } catch (JsonProcessingException e) {
            System.err.println("Error serializing event: " + e.getMessage());
        }
    }
}