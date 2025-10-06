package com.microservice.product_service.repository;

import com.microservice.product_service.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
	
	//Query for finding the products between the price range
    List<ProductModel> findByPriceBetween(Double minPrice, Double maxPrice);
    
    //Query for finding the products by the name
    List<ProductModel> findByNameContaining(String name);
    
    //query for finding the product category
    List<ProductModel> findByCategory(String category);
}