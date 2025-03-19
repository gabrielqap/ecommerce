package com.gabriel.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.service.PaymentService;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/{orderId}/pay")
    public Order processPayment(@PathVariable String orderId) {
        return paymentService.processPayment(orderId);
    }
}