package com.gabriel.ecommerce.messaging;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.exception.CustomJsonMappingException;
import com.gabriel.ecommerce.exception.CustomJsonProcessingException;
import com.gabriel.ecommerce.repository.OrderSummaryRepository;


@Service
public class OrderCreatedConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderCreatedConsumer.class);

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;


    @KafkaListener(topics = "order.created", groupId = "order-sync-group")
    public void syncOrderCreated(String message) {
        System.out.println("MESSAGE: " + message);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        OrderCreatedEventDTO event = null;
        try {
            event = objectMapper.readValue(message, OrderCreatedEventDTO.class);
        } catch (JsonMappingException e) {
            logger.error("Error mapping JSON to OrderCreatedEventDTO: {}", e.getMessage(), e);
            throw new CustomJsonMappingException("Invalid JSON format for OrderCreatedEventDTO", e);
        } catch (JsonProcessingException e) {
            logger.error("Error processing JSON: {}", e.getMessage(), e);
            throw new CustomJsonProcessingException("Error processing JSON data for OrderCreatedEventDTO", e);

        }
        String orderId = event.orderId();
        Double totalAmount = event.totalAmount();

        OrderSummary orderSummary = new OrderSummary();
        orderSummary.setId(orderId);
        orderSummary.setUserId(event.userId());
        orderSummary.setTotalAmount(totalAmount);
        orderSummary.setOrderCreatedAt(event.createdAt());
        orderSummary.setPaid(false);    

        orderSummaryRepository.save(orderSummary);
    }

    public static Date convertLocalDateTimeToSqlDate(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        
        LocalDate localDate = localDateTime.toLocalDate();
        return Date.valueOf(localDate);
    }

}