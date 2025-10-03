package com.microservice.product_service;

import com.microservice.product_service.model.ProductModel;
import com.microservice.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProductServiceApplicationTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void contextLoads() {
        // Basic context load
    }

    
}