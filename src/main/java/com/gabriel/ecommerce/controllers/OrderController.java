package com.gabriel.ecommerce.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.dto.AverageTicketDTO;
import com.gabriel.ecommerce.entity.dto.OrderDTO;
import com.gabriel.ecommerce.entity.dto.TopUserDTO;
import com.gabriel.ecommerce.entity.dto.TotalRevenueDTO;
import com.gabriel.ecommerce.service.OrderService;
import com.gabriel.ecommerce.service.OrderSummaryService;



@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderSummaryService orderSummaryService;

    @PostMapping
    public Order createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO.userId(), orderDTO.items());
    }

    @GetMapping("/top-users")
    public List<TopUserDTO> getTop5UsersByTotalSpent() {
        return orderSummaryService.getTop5UsersByTotalSpent();
    }

    @GetMapping("/average-ticket")
    public List<AverageTicketDTO> getAverageTicketPerUser() {
        return orderSummaryService.getAverageTicketPerUser();
    }

    @GetMapping("/total-revenue")
    public TotalRevenueDTO getTotalRevenueByMonthAndYear(
        @RequestParam int month,
        @RequestParam int year) {
        return orderSummaryService.getTotalRevenueByMonthAndYear(month, year);
    }

}