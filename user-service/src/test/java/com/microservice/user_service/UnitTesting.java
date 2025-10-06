package com.microservice.user_service;

import com.microservice.user_service.exception.UserException;
import com.microservice.user_service.model.UserModel;
import com.microservice.user_service.repository.UserRepository;
import com.microservice.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.microservice.user_service.model.Role.CUSTOMER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Logger logger;  

    @InjectMocks
    private UserService userService;

    private UserModel testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserModel(1L, "Test User", CUSTOMER, new ArrayList<>());
    }

    @Test
    void addUser_success() {
        when(userRepository.save(any(UserModel.class))).thenReturn(testUser);

        UserModel result = userService.addUser(testUser);

        assertNotNull(result);
        assertEquals("Test User", result.getName());
        verify(userRepository).save(testUser);
    }

    @Test
    void addUser_throwsExceptionWhenNameEmpty() {
        testUser.setName("");

        UserException exception = assertThrows(UserException.class, () -> userService.addUser(testUser));
        assertEquals("User name cannot be empty", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUser_success() {
        UserModel updated = new UserModel(1L, "Updated User", CUSTOMER, new ArrayList<>());

        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserModel.class))).thenReturn(updated);

        UserModel result = userService.updateUser(1L, updated);

        assertEquals("Updated User", result.getName());
        verify(userRepository).save(any());
    }

    @Test
    void updateUser_throwsExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.updateUser(1L, testUser));
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteUser_success() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUser_throwsExceptionWhenNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        UserException exception = assertThrows(UserException.class, () -> userService.deleteUser(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void getAllUsers_success() {
        List<UserModel> users = List.of(testUser);
        when(userRepository.findAll()).thenReturn(users);

        List<UserModel> result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void getUserById_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserModel result = userService.getUserById(1L);

        assertEquals("Test User", result.getName());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_throwsExceptionWhenNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserException exception = assertThrows(UserException.class, () -> userService.getUserById(1L));
        assertEquals("User not found with id: 1", exception.getMessage());
    }

    @Test
    void addOrderToUser_success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserModel.class))).thenReturn(testUser);

        userService.addOrderToUser(1L, 100L);

        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository).save(captor.capture());
        assertTrue(captor.getValue().getOrdersList().contains(100L));
    }
}