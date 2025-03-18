package com.gabriel.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gabriel.ecommerce.entity.OrderSummary;
import com.gabriel.ecommerce.entity.dto.AverageTicketDTO;
import com.gabriel.ecommerce.entity.dto.TopUserDTO;

public interface OrderSummaryRepository extends JpaRepository<OrderSummary, String> {
    List<TopUserDTO> findTop5ByPaidTrueOrderByTotalAmountDesc();

    @Query(value = """
        SELECT user_id AS userId, AVG(total_amount) AS averageTicket
        FROM order_summary
        GROUP BY user_id
        """, nativeQuery = true)
    List<AverageTicketDTO> findAverageTicketPerUser();

    @Query("SELECT SUM(os.totalAmount) FROM OrderSummary os WHERE os.paid = TRUE AND MONTH(os.createdAt) = :month AND YEAR(os.createdAt) = :year")
    Double findTotalRevenueByMonthAndYear(@Param("month") int month, @Param("year") int year);
}