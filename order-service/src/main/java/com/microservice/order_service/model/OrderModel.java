package com.microservice.order_service.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class OrderModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @ElementCollection
    @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "product_id")
    @Column(name = "quantity")
    private Map<Long, Integer> orderItems = new HashMap<>();  // productId -> quantity

    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // No-arg constructor (required for Jackson/JPA)
    public OrderModel() {}

    // All-arg constructor
    public OrderModel(Long id, Long userId, Map<Long, Integer> orderItems, Double totalPrice, OrderStatus status) {
        this.id = id;
        this.userId = userId;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Map<Long, Integer> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Map<Long, Integer> orderItems) {
        this.orderItems = orderItems;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}