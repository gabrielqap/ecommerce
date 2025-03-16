package com.gabriel.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.dto.AverageTicketDTO;
import com.gabriel.ecommerce.entity.dto.TopUserDTO;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, String> {
    @Query(value = """
        SELECT user_id AS userId, SUM(total_amount) AS totalSpent
        FROM order_summary
        WHERE paid = TRUE
        GROUP BY user_id
        ORDER BY totalSpent DESC
        LIMIT 5
        """, nativeQuery = true)
    List<TopUserDTO> findTop5UsersByTotalSpent();

    @Query(value = """
        SELECT user_id AS userId, AVG(total_amount) AS averageTicket
        FROM order_summary
        GROUP BY user_id
        """, nativeQuery = true)
    List<AverageTicketDTO> findAverageTicketPerUser();

    @Query(value = """
        SELECT SUM(total_amount) AS total_revenue
        FROM order_summary
        WHERE paid = TRUE
          AND MONTH(created_at) = :month
          AND YEAR(created_at) = :year
        """, nativeQuery = true)
    Double findTotalRevenueByMonthAndYear(
        @Param("month") int month,
        @Param("year") int year
    );
}