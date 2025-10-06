package com.microservice.order_service.controller;

import com.microservice.order_service.model.OrderModel;
import com.microservice.order_service.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Place a new order")
    @PostMapping
    public ResponseEntity<OrderModel> placeOrder(@RequestBody OrderModel order) {
        return ResponseEntity.ok(orderService.placeOrder(order));
    }

    @Operation(summary = "Get all orders")
    @GetMapping
    public ResponseEntity<List<OrderModel>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @Operation(summary = "Get order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getOrderById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @Operation(summary = "Get orders by user ID")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderModel>> getOrdersByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }
}