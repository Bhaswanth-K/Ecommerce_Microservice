package com.microservice.product_service.repository;

import com.microservice.product_service.model.ProductModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductModel, Long> {
    List<ProductModel> findByPriceBetween(Double minPrice, Double maxPrice);
    List<ProductModel> findByNameContaining(String name);
    List<ProductModel> findByCategory(String category);
}