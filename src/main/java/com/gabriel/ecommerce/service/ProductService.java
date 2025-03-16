package com.gabriel.ecommerce.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.repository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public Product indexProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(
                name,
                category,
                minPrice,
                maxPrice,
                0
        );
    }

    public List<Product> searchProducts_(String name, String category, Double minPrice, Double maxPrice) {
        Criteria criteria = new Criteria();

        
        if (name != null) {
            criteria.and("name").is(name);
        }
        if (category != null) {
            criteria.and("category").is(category);
        }
        if (minPrice != null && maxPrice != null) {
            criteria.and("price").between(minPrice, maxPrice);
        }
        
        criteria.and("stock").greaterThan(0);
        
        CriteriaQuery query = new CriteriaQuery(criteria);
        return elasticsearchOperations.search(query, Product.class)
                .stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    public void updateStock(String productId, Integer quantitySold) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        product.setStock(product.getStock() - quantitySold);
        productRepository.save(product);
    }

    public Product updateProduct(String id, Product updatedProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setCategory(updatedProduct.getCategory());
            product.setPrice(updatedProduct.getPrice());
            product.setStock(updatedProduct.getStock());
            return productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public Product patchProduct(String id, Product partialProduct) {
        Optional<Product> existingProduct = productRepository.findById(id);

        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();

            if (partialProduct.getName() != null) {
                product.setName(partialProduct.getName());
            }
            if (partialProduct.getCategory() != null) {
                product.setCategory(partialProduct.getCategory());
            }
            if (partialProduct.getPrice() != null) {
                product.setPrice(partialProduct.getPrice());
            }
            if (partialProduct.getStock() != null) {
                product.setStock(partialProduct.getStock());
            }

            return productRepository.save(product);
        } else {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
    }

    public void deleteProduct(String id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with ID: " + id);
        }
        productRepository.deleteById(id);
    }

}