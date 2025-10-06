package com.microservice.product_service;

import com.microservice.product_service.exception.ProductException;
import com.microservice.product_service.model.ProductModel;
import com.microservice.product_service.repository.ProductRepository;
import com.microservice.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private ProductModel testProduct;

    @BeforeEach
    void setUp() {
        testProduct = new ProductModel(1L, "Test Product", "Description", "Category", 10.0, 5);
    }

    @Test
    
    //add product
    void addProduct_success() {
        when(productRepository.save(any(ProductModel.class))).thenReturn(testProduct);

        ProductModel result = productService.addProduct(testProduct);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productRepository).save(testProduct);
    }

    @Test
    //Adding the product with negative number for the setting the quantity
    void addProduct_throwsExceptionWhenQuantityNegative() {
        testProduct.setQuantity(-1);

        ProductException exception = assertThrows(ProductException.class, () -> productService.addProduct(testProduct));
        assertEquals("Quantity cannot be negative", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    //checking all the products
    void getAllProducts_success() {
        List<ProductModel> products = List.of(testProduct);
        when(productRepository.findAll()).thenReturn(products);

        List<ProductModel> result = productService.getAllProducts();

        assertEquals(1, result.size());
        verify(productRepository).findAll();
    }

    @Test
    //checking the product by id when product is available
    void getProductById_success() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

        ProductModel result = productService.getProductById(1L);

        assertEquals("Test Product", result.getName());
        verify(productRepository).findById(1L);
    }

    @Test
    //checking product by id when product not available 
    void getProductById_throwsExceptionWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.getProductById(1L));
        assertEquals("Product not found with id: 1", exception.getMessage());
    }

    @Test
    //updating product when product is available
    void updateProduct_success() {
        ProductModel updated = new ProductModel(1L, "Updated Product", "New Desc", "New Cat", 20.0, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
        when(productRepository.save(any(ProductModel.class))).thenReturn(updated);

        ProductModel result = productService.updateProduct(1L, updated);

        assertEquals("Updated Product", result.getName());
        assertEquals(20.0, result.getPrice());
        verify(productRepository).save(any());
    }

    @Test
    //updating product when product is not available
    void updateProduct_throwsExceptionWhenNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.updateProduct(1L, testProduct));
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, never()).save(any());
    }

    @Test
    // deleting product when product is available
    void deleteProduct_success() {
        when(productRepository.existsById(1L)).thenReturn(true);

        productService.deleteProduct(1L);

        verify(productRepository).deleteById(1L);
    }

    @Test
    //deleting product with exception when no product found
    void deleteProduct_throwsExceptionWhenNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductException exception = assertThrows(ProductException.class, () -> productService.deleteProduct(1L));
        assertEquals("Product not found with id: 1", exception.getMessage());
        verify(productRepository, never()).deleteById(any());
    }

    @Test
    //getting product by price range
    void getProductsByPriceRange_success() {
        List<ProductModel> products = List.of(testProduct);
        when(productRepository.findByPriceBetween(5.0, 15.0)).thenReturn(products);

        List<ProductModel> result = productService.getProductsByPriceRange(5.0, 15.0);

        assertEquals(1, result.size());
        verify(productRepository).findByPriceBetween(5.0, 15.0);
    }

    @Test
    //getting product by name
    void getProductsByName_success() {
        List<ProductModel> products = List.of(testProduct);
        when(productRepository.findByNameContaining("Test")).thenReturn(products);

        List<ProductModel> result = productService.getProductsByName("Test");

        assertEquals(1, result.size());
        verify(productRepository).findByNameContaining("Test");
    }

    @Test
    // getting products by category
    void getProductsByCategory_success() {
        List<ProductModel> products = List.of(testProduct);
        when(productRepository.findByCategory("Category")).thenReturn(products);

        List<ProductModel> result = productService.getProductsByCategory("Category");

        assertEquals(1, result.size());
        verify(productRepository).findByCategory("Category");
    }
}