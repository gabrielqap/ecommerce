package com.gabriel.ecommerce.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.entity.dto.OrderCreatedEventDTO;
import com.gabriel.ecommerce.entity.dto.OrderItemDTO;
import com.gabriel.ecommerce.enums.OrderStatus;
import com.gabriel.ecommerce.exception.CustomJsonMappingException;
import com.gabriel.ecommerce.exception.CustomJsonProcessingException;
import com.gabriel.ecommerce.exception.OrderNotFoundException;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.repository.OrderRepository;
import com.gabriel.ecommerce.repository.OrderSummaryRepository;
import com.gabriel.ecommerce.repository.ProductRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderPaidConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderPaidConsumer.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @KafkaListener(topics = "order.paid", groupId = "order-group")
    public void consumeOrderPaid(String message) {
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

        updateOrderStatusOnElastic(event.orderId(), OrderStatus.PAID);
        updateOrderStatusOnSQLToPaid(event.orderId());
        
        for (OrderItemDTO item : event.items()) {
            updateProductStock(item.getProductId(), item.getQuantity());
        }
    }
        
    private void updateOrderStatusOnSQLToPaid(String orderId) {
        OrderSummary orderSummary = orderSummaryRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        orderSummary.setPaid(true);
        orderSummaryRepository.save(orderSummary);
    }
        
    private void updateOrderStatusOnElastic(String orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        order.setStatus(status);
        orderRepository.save(order);

        logger.info("Updated status for order " + orderId + " to " + status);
    }

    

    private void updateProductStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        logger.info("Updated stock for product " + productId + ". Reduced by: " + quantity);
    }
}