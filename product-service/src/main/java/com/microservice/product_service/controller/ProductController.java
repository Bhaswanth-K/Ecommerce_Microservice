package com.microservice.product_service.controller;

import com.microservice.product_service.model.ProductModel;
import com.microservice.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Add a new product (Admin only)") 
    @PostMapping
    public ResponseEntity<ProductModel> addProduct(@RequestBody ProductModel product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @Operation(summary = "Get all products (Open to all)")
    @GetMapping
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @Operation(summary = "Get product by ID (Open to all)")
    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProductById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Update a product by ID (Admin only)")
    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> updateProduct(@PathVariable("id") Long id, @RequestBody ProductModel updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(id, updatedProduct));
    }

    @Operation(summary = "Delete a product by ID (Admin only)")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully with id: " + id);
    }


    @Operation(summary = "Filter products by price range (Open to all)") 
    @GetMapping("/filter/price")
    public ResponseEntity<List<ProductModel>> getByPriceRange(@RequestParam("min") Double min, @RequestParam("max") Double max) {
        return ResponseEntity.ok(productService.getProductsByPriceRange(min, max));
    }

    @Operation(summary = "Filter products by name (Open to all)")
    @GetMapping("/filter/name")
    public ResponseEntity<List<ProductModel>> getByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(productService.getProductsByName(name));
    }

    @Operation(summary = "Filter products by category (Open to all)")
    @GetMapping("/filter/category")
    public ResponseEntity<List<ProductModel>> getByCategory(@RequestParam("category") String category) {
        return ResponseEntity.ok(productService.getProductsByCategory(category));
    }
}