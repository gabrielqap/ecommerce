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
          #{
            if (#name != null) {
              {'match': {'name': '?0'}}
            }
          },
          #{
            if (#category != null) {
              {'match': {'category': '?1'}}
            }
          },
          #{
            if (#minPrice != null || #maxPrice != null) {
              {'range': {'price': {
                #{if (#minPrice != null) {'gte': ?2}},
                #{if (#maxPrice != null) {'lte': ?3}}
              }}}
            }
          },
          #{
            if (#stock != null) {
              {'range': {'stock': {'gt': ?4}}}
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
