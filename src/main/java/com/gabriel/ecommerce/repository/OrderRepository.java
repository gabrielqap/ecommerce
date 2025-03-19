package com.gabriel.ecommerce.repository;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.gabriel.ecommerce.entity.Order;

import java.util.List;

public interface OrderRepository extends ElasticsearchRepository<Order, String> {
    List<Order> findByUserId(String userId);
    
    @Query("{\"nested\": {\"path\": \"items\", \"query\": {\"term\": {\"items.product_id\": ?0}}}}")
    long countByProductId(String productId);
}