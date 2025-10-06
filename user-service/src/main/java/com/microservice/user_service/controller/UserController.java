package com.microservice.user_service.controller;

import com.microservice.user_service.model.UserModel;
import com.microservice.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Add a new user")
    @PostMapping
    public ResponseEntity<UserModel> addUser(@RequestBody UserModel user) {
        return ResponseEntity.ok(userService.addUser(user));
    }

    @Operation(summary = "Update a user by ID")
    @PutMapping("/{id}")
    public ResponseEntity<UserModel> updateUser(@PathVariable("id") Long id, @RequestBody UserModel user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @Operation(summary = "Delete a user by ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully with id: " + id);
    }


    @Operation(summary = "Get all users")
    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Get user by ID") //It includes orders list to show the orders of the user placed successfully
    @GetMapping("/{id}")
    public ResponseEntity<UserModel> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Add order to user") //For internal use by feign client
    @PostMapping("/{userId}/orders")
    public void addOrderToUser(@PathVariable("userId") Long userId, @RequestParam("orderId") Long orderId) {
        userService.addOrderToUser(userId, orderId);
    }
}