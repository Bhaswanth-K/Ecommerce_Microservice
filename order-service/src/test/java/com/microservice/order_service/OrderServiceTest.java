package com.microservice.order_service;

import com.microservice.order_service.client.ProductClient;
import com.microservice.order_service.client.UserClient;
import com.microservice.order_service.common.ProductModel;
import com.microservice.order_service.common.Role;
import com.microservice.order_service.common.UserModel;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.model.OrderModel;
import com.microservice.order_service.model.OrderStatus;
import com.microservice.order_service.repository.OrderRepository;
import com.microservice.order_service.service.OrderService;

import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private OrderService orderService;

    private OrderModel testOrder;
    private ProductModel testProduct;
    private UserModel testUser;

    @BeforeEach
    void setUp() {
        Map<Long, Integer> items = new HashMap<>();
        items.put(1L, 2);

        testOrder = new OrderModel(null, 1L, items, null, null);
        testProduct = new ProductModel(1L, "Test Product", "Desc", "Cat", 10.0, 5);
        testUser = new UserModel(1L, "Test User", Role.CUSTOMER, List.of());
    }

    @Test
    void placeOrder_success() {
        when(userClient.getUserById(1L)).thenReturn(testUser);
        when(productClient.getProductById(1L)).thenReturn(testProduct);
        when(productClient.updateProduct(eq(1L), any(ProductModel.class))).thenReturn(testProduct);
        when(orderRepository.save(any(OrderModel.class))).thenReturn(new OrderModel(1L, 1L, testOrder.getOrderItems(), 20.0, OrderStatus.PLACED));
        doNothing().when(userClient).addOrderToUser(1L, 1L);

        OrderModel result = orderService.placeOrder(testOrder);

        assertNotNull(result);
        assertEquals(20.0, result.getTotalPrice());
        assertEquals(OrderStatus.PLACED, result.getStatus());
        verify(productClient).updateProduct(eq(1L), argThat(p -> p.getQuantity() == 3));
        verify(userClient).addOrderToUser(1L, 1L);
        verify(orderRepository).save(any(OrderModel.class));
    }

    @Test
    void placeOrder_throwsWhenUserNotFound() {
        when(userClient.getUserById(1L)).thenThrow(FeignException.class);

        OrderException exception = assertThrows(OrderException.class, () -> orderService.placeOrder(testOrder));
        assertEquals("User not found with id: 1", exception.getMessage());
        verifyNoInteractions(productClient, orderRepository);
    }

    @Test
    void placeOrder_throwsWhenProductNotFound() {
        when(userClient.getUserById(1L)).thenReturn(testUser);
        when(productClient.getProductById(1L)).thenReturn(null);

        OrderException exception = assertThrows(OrderException.class, () -> orderService.placeOrder(testOrder));
        assertEquals("Product not found with id: 1", exception.getMessage());
        verifyNoMoreInteractions(productClient);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void placeOrder_throwsWhenInsufficientQuantity() {
        testOrder.getOrderItems().put(1L, 10); // More than available
        when(userClient.getUserById(1L)).thenReturn(testUser);
        when(productClient.getProductById(1L)).thenReturn(testProduct);

        OrderException exception = assertThrows(OrderException.class, () -> orderService.placeOrder(testOrder));
        assertEquals("Insufficient quantity for product: 1", exception.getMessage());
        verifyNoMoreInteractions(productClient);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void getAllOrders_success() {
        List<OrderModel> orders = List.of(testOrder);
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderModel> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        verify(orderRepository).findAll();
    }

    @Test
    void getOrderById_success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        OrderModel result = orderService.getOrderById(1L);

        assertEquals(1L, result.getUserId());
        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_throwsWhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderException exception = assertThrows(OrderException.class, () -> orderService.getOrderById(1L));
        assertEquals("Order not found with id: 1", exception.getMessage());
    }

    @Test
    void getOrdersByUserId_success() {
        List<OrderModel> orders = List.of(testOrder);
        when(orderRepository.findByUserId(1L)).thenReturn(orders);

        List<OrderModel> result = orderService.getOrdersByUserId(1L);

        assertEquals(1, result.size());
        verify(orderRepository).findByUserId(1L);
    }
}