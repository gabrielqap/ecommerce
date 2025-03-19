package com.gabriel.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.dto.AverageTicketDTO;
import com.gabriel.ecommerce.entity.dto.TopUserDTO;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, String> {
    @Query("""
        SELECT new com.gabriel.ecommerce.entity.dto.TopUserDTO(u.username, SUM(o.totalAmount))
        FROM OrderSummary o
        JOIN User u ON o.userId = u.id
        WHERE o.paid = true
        GROUP BY u.username
        ORDER BY SUM(o.totalAmount) DESC 
        LIMIT 5
    """)
    List<TopUserDTO> findTop5ByPaidTrueOrderByTotalAmountDesc();

    @Query("""
        SELECT new com.gabriel.ecommerce.entity.dto.AverageTicketDTO(u.username, AVG(o.totalAmount))
        FROM OrderSummary o
        JOIN User u ON o.userId = u.id
        GROUP BY u.username
    """)
    List<AverageTicketDTO> findAverageTicketPerUser();

    @Query("SELECT SUM(os.totalAmount) FROM OrderSummary os WHERE os.paid = TRUE AND MONTH(os.orderCreatedAt) = :month AND YEAR(os.orderCreatedAt) = :year")
    Double findTotalRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);
}