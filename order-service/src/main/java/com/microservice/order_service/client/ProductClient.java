package com.microservice.order_service.client;

import com.microservice.order_service.common.ProductModel;  
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductClient {
	
	//To fetch the product by id from the product service 
    @GetMapping("/products/{id}")
    ProductModel getProductById(@PathVariable("id") Long id);
    
    // update the product by id to the product in product service
    @PutMapping("/products/{id}")
    ProductModel updateProduct(@PathVariable("id") Long id, @RequestBody ProductModel product);
}