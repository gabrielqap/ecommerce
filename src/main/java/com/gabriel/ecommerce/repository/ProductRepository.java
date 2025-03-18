package com.gabriel.ecommerce.repository;

import java.util.List;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;

import com.gabriel.ecommerce.entity.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
  @Query("""
    {
      "bool": {
        "must": [
          { "match": { "name": "?0" } },
          { "match": { "category": "?1" } }
        ],
        "filter": [
          {
            "range": {
              "price": {
                "gte": "?2",
                "lte": "?3"
              }
            }
          },
          {
            "range": {
              "stock": {
                "gt": "?4"
              }
            }
          }
        ]
      }
    }
    """)
  List<Product> searchProducts(
          @Param("name") String name,
          @Param("category") String category,
          @Param("minPrice") Double minPrice,
          @Param("maxPrice") Double maxPrice,
          @Param("stock") Integer stock
  );
  List<Product> findAllById(List<String> ids); // Retorna uma List<Product>
}
