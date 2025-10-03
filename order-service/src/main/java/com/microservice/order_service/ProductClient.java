package com.microservice.order_service;

import com.microservice.order_service.common.ProductModel;  // Assume shared model or DTO
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductModel getProductById(@PathVariable("id") Long id);

    @PutMapping("/products/{id}")
    ProductModel updateProduct(@PathVariable("id") Long id, @RequestBody ProductModel product);
}