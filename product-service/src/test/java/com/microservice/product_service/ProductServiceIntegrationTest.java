package com.microservice.product_service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microservice.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void fullFlow_addUpdateDeleteProduct() throws Exception {

        productRepository.deleteAll();

        // Adding product and capture response
        MvcResult result = mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Integration Test Product\", \"description\":\"Test Desc\", \"category\":\"Test Phone\", \"price\":155.0, \"quantity\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Product"))
                .andReturn();

        // Extracting product ID from response
        String responseContent = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseContent);
        Long productId = jsonNode.get("id").asLong();

        // TO get product
        mockMvc.perform(get("/products/" + productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Product"));

        // Update product
        mockMvc.perform(put("/products/" + productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Integration Product\", \"description\":\"Updated Desc\", \"category\":\"Updated Phone\", \"price\":255.0, \"quantity\":6}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Integration Product"));

        // Get by price range
        mockMvc.perform(get("/products/filter/price")
                .param("min", "200.0")
                .param("max", "300.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Updated Integration Product"));

        // Get by name
        mockMvc.perform(get("/products/filter/name")
                .param("name", "Updated"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Updated Integration Product"));

        // Get by category
        mockMvc.perform(get("/products/filter/category")
                .param("category", "Updated Phone"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Updated Integration Product"));

        // Delete product
        mockMvc.perform(delete("/products/" + productId))
                .andExpect(status().isOk());

        // Verify deleted
        mockMvc.perform(get("/products/" + productId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllProducts_emptyInitially() throws Exception {
        productRepository.deleteAll();

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }
}