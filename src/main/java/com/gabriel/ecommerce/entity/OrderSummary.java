package com.gabriel.ecommerce.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Entity
@Table(name = "order_summary")
@Data
public class OrderSummary {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;
    private Double totalAmount;
    
    @Temporal(TemporalType.DATE) // Mapeia para o tipo DATE no banco de dados
    @Column(name = "order_created_at", nullable = false)
    private Date orderCreatedAt;
    private Boolean paid;
}