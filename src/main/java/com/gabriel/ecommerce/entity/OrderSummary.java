package com.gabriel.ecommerce.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "order_summary")
@Data
public class OrderSummary {

    @Id
    private String id;

    private String userId;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private Boolean paid;
}