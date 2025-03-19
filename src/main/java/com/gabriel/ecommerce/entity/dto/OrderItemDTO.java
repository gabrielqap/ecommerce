package com.gabriel.ecommerce.entity.dto;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.Data;

@Data
public class OrderItemDTO {

    @Field(type = FieldType.Keyword, name = "product_id")
    private String productId;

    @Field(type = FieldType.Integer, name = "quantity")
    private Integer quantity;
}