package com.microservice.product_service.service;

import com.microservice.product_service.exception.ProductException;
import com.microservice.product_service.model.ProductModel;
import com.microservice.product_service.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    
    //add products
    public ProductModel addProduct(ProductModel product) {
        logger.info("Adding product: {}", product.getName());
        if (product.getQuantity() < 0) {
            throw new ProductException("Quantity cannot be negative");
        }
        return productRepository.save(product);
    }
    
    //get all products
    public List<ProductModel> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll();
    }
    
    //get product by id 
    public ProductModel getProductById(Long id) {
        logger.info("Fetching product by id: {}", id);
        return productRepository.findById(id).orElseThrow(() -> new ProductException("Product not found with id: " + id));
    }
    
    // update product by id
    public ProductModel updateProduct(Long id, ProductModel updated) { 
        logger.info("Updating product with id: {}", id);
        ProductModel existing = getProductById(id);  
        existing.setName(updated.getName());
        existing.setDescription(updated.getDescription());
        existing.setCategory(updated.getCategory());
        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());
        return productRepository.save(existing);
    }
    
    //delete product by id
    public void deleteProduct(Long id) {
        logger.info("Deleting product with id: {}", id);

        if (!productRepository.existsById(id)) {
            throw new ProductException("Product not found with id: " + id);
        }

        productRepository.deleteById(id);
        logger.info("Product deleted successfully with id: {}", id);
    }

    //for getting the products by price range
    public List<ProductModel> getProductsByPriceRange(Double minPrice, Double maxPrice) {
        logger.info("Fetching products in price range: {}-{}", minPrice, maxPrice);
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }
    
    //for getting the products by name
    public List<ProductModel> getProductsByName(String name) {
        logger.info("Fetching products by name: {}", name);
        return productRepository.findByNameContaining(name);
    }
    
    // For getting the products by category
    public List<ProductModel> getProductsByCategory(String category) {
        logger.info("Fetching products by category: {}", category);
        return productRepository.findByCategory(category);
    }
}