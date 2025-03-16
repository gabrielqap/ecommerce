package com.gabriel.ecommerce.entity;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.gabriel.ecommerce.entity.dto.OrderItemDTO;
import com.gabriel.ecommerce.enums.OrderStatus;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(indexName = "orders")
public class Order {

    @Id
    private String id;

    @Field(type = FieldType.Keyword, name = "user_id")
    private String userId;

    @Field(type = FieldType.Nested, name = "items")
    private List<OrderItemDTO> items;

    @Field(type = FieldType.Keyword, name = "status")
    private OrderStatus status;

    @Field(type = FieldType.Date, name = "created_at")
    private LocalDateTime createdAt;

    @Field(type = FieldType.Date, name = "updated_at")
    private LocalDateTime updatedAt;
}