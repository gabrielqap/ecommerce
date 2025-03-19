package com.gabriel.ecommerce.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.gabriel.ecommerce.entity.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
  List<Product> findAllById(List<String> ids);
}
