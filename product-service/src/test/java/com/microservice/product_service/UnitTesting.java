package com.microservice.product_service;

import com.microservice.product_service.exception.ProductException;
import com.microservice.product_service.model.ProductModel;
import com.microservice.product_service.repository.ProductRepository;
import com.microservice.product_service.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnitTesting {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

  
   

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        assertTrue(productService.getAllProducts().isEmpty());
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);
        assertThrows(ProductException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(1L)).thenReturn(true);
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}