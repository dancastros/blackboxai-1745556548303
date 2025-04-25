package com.hortensia.service;

import com.hortensia.model.Order;
import com.hortensia.model.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getAllOrders();
    Optional<Order> getOrderById(Long id);
    List<Order> getOrdersByUser(User user);
    List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    Order createOrder(Order order);
    void deleteOrder(Long id);
    Order updateOrder(Long id, Order orderDetails);
    List<Order> getUserOrdersOrderedByDateDesc(User user);
    boolean validateOrderStock(Order order);
    void processOrder(Order order);
}
