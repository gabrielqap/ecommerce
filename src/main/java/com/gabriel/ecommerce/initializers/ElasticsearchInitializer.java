package com.gabriel.ecommerce.initializers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Component;

import com.gabriel.ecommerce.entity.Order;
import com.gabriel.ecommerce.entity.Product;

@Component
public class ElasticsearchInitializer implements CommandLineRunner {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Override
    public void run(String... args) throws Exception {
        createIndexIfNotExists(Product.class, "products");
        createIndexIfNotExists(Order.class, "orders");
    }

    private void createIndexIfNotExists(Class<?> clazz, String indexName) {
        IndexOperations indexOps = elasticsearchOperations.indexOps(clazz);
        if (!indexOps.exists()) {
            indexOps.create();
            indexOps.putMapping(indexOps.createMapping());
            System.out.println("Index '" + indexName + "' created successfully.");
        } else {
            System.out.println("Index '" + indexName + "' already exists.");
        }
    }
}