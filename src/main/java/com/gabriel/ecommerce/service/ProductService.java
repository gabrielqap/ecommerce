package com.gabriel.ecommerce.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gabriel.ecommerce.entity.Product;
import com.gabriel.ecommerce.exception.ProductNotFoundException;
import com.gabriel.ecommerce.repository.OrderRepository;
import com.gabriel.ecommerce.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

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
        
        long orderCount = orderRepository.countByProductId(id);

        if (orderCount > 0) {
            throw new IllegalStateException("Cannot delete product with ID " + id + " because it is associated with " + orderCount + " orders.");
        }

        productRepository.deleteById(id);
    }

}