package com.microservice.order_service.service;

import com.microservice.order_service.client.ProductClient;
import com.microservice.order_service.client.UserClient;
import com.microservice.order_service.exception.OrderException;
import com.microservice.order_service.model.OrderModel;
import com.microservice.order_service.model.OrderStatus;
import com.microservice.order_service.common.ProductModel;  
import com.microservice.order_service.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductClient productClient;  

    @Autowired
    private UserClient userClient;  
    
    //adding a new order
    public OrderModel placeOrder(OrderModel order) {
        logger.info("Placing order for user: {}", order.getUserId());

        
        try {
            userClient.getUserById(order.getUserId());
        } catch (Exception e) {
            throw new OrderException("User not found with id: " + order.getUserId());
        }

        double calculatedPrice = 0.0;

        
        for (Map.Entry<Long, Integer> item : order.getOrderItems().entrySet()) {
            Long productId = item.getKey();
            Integer quantity = item.getValue();

            ProductModel product = productClient.getProductById(productId);
            if (product == null) {
                throw new OrderException("Product not found with id: " + productId);
            }
            if (product.getQuantity() < quantity) {
                throw new OrderException("Insufficient quantity for product: " + productId);
            }

            
            product.setQuantity(product.getQuantity() - quantity);
            productClient.updateProduct(productId, product);

            calculatedPrice += product.getPrice() * quantity;
        }

        order.setTotalPrice(calculatedPrice);
        order.setStatus(OrderStatus.PLACED);
        OrderModel savedOrder = orderRepository.save(order);

        
        userClient.addOrderToUser(order.getUserId(), savedOrder.getId());

        return savedOrder;
    }
    
    //fetching all orders
    public List<OrderModel> getAllOrders() {
        logger.info("Fetching all orders");
        return orderRepository.findAll();
    }
    
    //fetching orders by id
    public OrderModel getOrderById(Long id) {
        logger.info("Fetching order by id: {}", id);
        return orderRepository.findById(id).orElseThrow(() -> new OrderException("Order not found with id: " + id));
    }
    
    //fetching orders by user id
    public List<OrderModel> getOrdersByUserId(Long userId) {
        logger.info("Fetching orders for user: {}", userId);
        return orderRepository.findByUserId(userId);
    }
}