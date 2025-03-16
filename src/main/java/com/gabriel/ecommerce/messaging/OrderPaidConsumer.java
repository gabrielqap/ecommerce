package com.gabriel.ecommerce.messaging;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.entity.dto.OrderItemDTO;
import com.gabriel.ecommerce.entity.dto.OrderPaidEventDTO;
import com.gabriel.ecommerce.enums.OrderStatus;
import com.gabriel.ecommerce.exception.OrderNotFoundException;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.repository.OrderRepository;
import com.gabriel.ecommerce.repository.OrderSummaryRepository;
import com.gabriel.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class OrderPaidConsumer {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    @KafkaListener(topics = "order.paid", groupId = "order-group")
    public void consumeOrderPaid(OrderPaidEventDTO event) {
        System.out.println("Received order.paid event: " + event);

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

        System.out.println("Updated status for order " + orderId + " to " + status);
    }

    

    private void updateProductStock(String productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        System.out.println("Updated stock for product " + productId + ". Reduced by: " + quantity);
    }
}