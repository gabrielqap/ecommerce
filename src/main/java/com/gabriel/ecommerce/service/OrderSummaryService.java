package com.gabriel.ecommerce.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.dto.AverageTicketDTO;
import com.gabriel.ecommerce.entity.dto.TopUserDTO;
import com.gabriel.ecommerce.entity.dto.TotalRevenueDTO;
import com.gabriel.ecommerce.repository.OrderSummaryRepository;

@Service
public class OrderSummaryService {

    @Autowired
    private OrderSummaryRepository orderSummaryRepository;

    public List<TopUserDTO> getTop5UsersByTotalSpent() {
        return orderSummaryRepository.findTop5UsersByTotalSpent();
    }

    public List<AverageTicketDTO> getAverageTicketPerUser() {
        return  orderSummaryRepository.findAverageTicketPerUser();
    }

    public TotalRevenueDTO getTotalRevenueByMonthAndYear(int month, int year) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month. It must be between 1 and 12.");
        }
        
        Double totalRevenue = orderSummaryRepository.findTotalRevenueByMonthAndYear(month, year);
        return new TotalRevenueDTO(totalRevenue != null ? totalRevenue : 0.0);
    }
}