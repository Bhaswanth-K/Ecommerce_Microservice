package com.microservice.user_service.service;

import com.microservice.user_service.exception.UserException;
import com.microservice.user_service.model.UserModel;
import com.microservice.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public UserModel addUser(UserModel user) {
        logger.info("Adding user: {}", user.getName());
        if (user.getName() == null || user.getName().isEmpty()) {
            throw new UserException("User name cannot be empty");
        }
        return userRepository.save(user);
    }

    public UserModel updateUser(Long id, UserModel updatedUser) {
        logger.info("Updating user with id: {}", id);
        UserModel existing = userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id: " + id));
        existing.setName(updatedUser.getName());
        existing.setRole(updatedUser.getRole());
        // Update ordersList if needed: existing.setOrdersList(updatedUser.getOrdersList());
        return userRepository.save(existing);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public List<UserModel> getAllUsers() {
        logger.info("Fetching all users");
        return userRepository.findAll();
    }

    public UserModel getUserById(Long id) {
        logger.info("Fetching user by id: {}", id);
        return userRepository.findById(id).orElseThrow(() -> new UserException("User not found with id: " + id));
    }

    public void addOrderToUser(Long userId, Long orderId) {  // Ensure this method exists
        logger.info("Added order {} to user {}", orderId, userId);
        UserModel user = getUserById(userId);
        user.getOrdersList().add(orderId);
        userRepository.save(user);
    }
}