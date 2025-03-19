package com.gabriel.ecommerce.service;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.entity.dto.OrderItemDTO;
import com.gabriel.ecommerce.entity.dto.OrderPaidEventDTO;
import com.gabriel.ecommerce.enums.OrderStatus;
import com.gabriel.ecommerce.exception.InsufficientStockException;
import com.gabriel.ecommerce.exception.OrderAlreadyProcessedException;
import com.gabriel.ecommerce.exception.OrderNotFoundException;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.messaging.OrderEventProducer;
import com.gabriel.ecommerce.repository.OrderRepository;
import com.gabriel.ecommerce.repository.ProductRepository;


@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderEventProducer orderEventProducer;

    @Autowired
    private NotificationService notificationService;

    public Order processPayment(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found: " + orderId));

        notificationService.notifyUser(
            order.getUserId(),
            order.getId(),
            "Your order #" + order.getId() + " has been canceled due to insufficient stock."
        );

        if (order.getStatus() != OrderStatus.PENDING) {
            logger.error("Order " + orderId +" already processed or canceled.");
            throw new OrderAlreadyProcessedException("Order already processed or canceled.");
        }

        try {
            validateStock(order.getItems());

            process();

            OrderPaidEventDTO event = new OrderPaidEventDTO(order.getId(), order.getItems());
            orderEventProducer.sendOrderPaidEvent(event);

            order.setStatus(OrderStatus.PAID);
            return orderRepository.save(order);

        } catch (InsufficientStockException e) {
            order.setStatus(OrderStatus.CANCELED);
            orderRepository.save(order);
            logger.error("Order " + order.getId() + " has been canceled due to insufficient stock.");
            notificationService.notifyUser(
                order.getUserId(),
                order.getId(),
                "Your order #" + order.getId() + " has been canceled due to insufficient stock."
            );
            
            throw e;
        }
    }

    private void validateStock(List<OrderItemDTO> items) {
        for (OrderItemDTO item : items) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new ProductNotFoundException("Product not found: " + item.getProductId()));

                    if (product.getStock() < item.getQuantity()) {
                        logger.error("Insufficient stock for product: " + product.getName() + ". The order has been canceled.");
                        throw new InsufficientStockException(
                            "Insufficient stock for product: " + product.getName() + ". The order has been canceled."
                        );
                    }
                    
        }
    }

    public void process(){
        logger.info("Processing payment...");
    }
}