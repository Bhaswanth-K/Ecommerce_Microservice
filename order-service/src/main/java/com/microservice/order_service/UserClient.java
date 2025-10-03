package com.microservice.order_service;

import com.microservice.order_service.common.UserModel;  // Assume shared or DTO
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserModel getUserById(@PathVariable("id") Long id);

    @PostMapping("/users/{userId}/orders")
    void addOrderToUser(@PathVariable("userId") Long userId, @RequestParam("orderId") Long orderId);
}