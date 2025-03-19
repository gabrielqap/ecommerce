package com.gabriel.ecommerce.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.entity.dto.OrderItemDTO;
import com.gabriel.ecommerce.enums.OrderStatus;
import com.gabriel.ecommerce.exception.InsufficientStockException;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.repository.OrderRepository;
import com.gabriel.ecommerce.repository.ProductRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderEventService orderEventService;

    public Order createOrder(String userId, List<OrderItemDTO> items) {
        List<String> productIds = items.stream()
                .map(OrderItemDTO::getProductId)
                .collect(Collectors.toList());

        Map<String, Product> products = productRepository.findAllById(productIds)
        .stream()
        .collect(Collectors.toMap(Product::getId, Function.identity()));

        validateStock(items, products);
        double totalAmount = calculateTotalAmount(items, products);

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(items);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now()); 
        Order savedOrder = orderRepository.save(order);

        orderEventService.publishOrderCreatedEvent(savedOrder, totalAmount);

        return savedOrder;
    }

    private void validateStock(List<OrderItemDTO> items, Map<String, Product> products) {
        for (OrderItemDTO item : items) {
            Product product = products.get(item.getProductId());
            if (product == null) {
                throw new ProductNotFoundException("Product not found: " + item.getProductId());
            }
            if (product.getStock() < item.getQuantity()) {
                logger.info("LOGGED OK");
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    private double calculateTotalAmount(List<OrderItemDTO> items, Map<String, Product> products) {
        return items.stream()
                .mapToDouble(item -> {
                    Product product = products.get(item.getProductId());
                    return product.getPrice() * item.getQuantity();
                })
                .sum();
    }
}