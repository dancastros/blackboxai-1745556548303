package com.hortensia.service.impl;

import com.hortensia.model.Order;
import com.hortensia.model.OrderItem;
import com.hortensia.model.User;
import com.hortensia.repository.OrderRepository;
import com.hortensia.service.OrderService;
import com.hortensia.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    
    private final OrderRepository orderRepository;
    private final ProductService productService;
    
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, ProductService productService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
    }
    
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Override
    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }
    
    @Override
    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUser(user);
    }
    
    @Override
    public List<Order> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    @Override
    @Transactional
    public Order createOrder(Order order) {
        if (!validateOrderStock(order)) {
            throw new RuntimeException("Insufficient stock for one or more products");
        }
        return orderRepository.save(order);
    }
    
    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id)
            .map(order -> {
                order.setUser(orderDetails.getUser());
                order.setTotalAmount(orderDetails.getTotalAmount());
                order.setItems(orderDetails.getItems());
                return orderRepository.save(order);
            })
            .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
    }
    
    @Override
    public List<Order> getUserOrdersOrderedByDateDesc(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }
    
    @Override
    public boolean validateOrderStock(Order order) {
        for (OrderItem item : order.getItems()) {
            if (!productService.isStockAvailable(item.getProduct().getId(), item.getQuantity())) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    @Transactional
    public void processOrder(Order order) {
        if (!validateOrderStock(order)) {
            throw new RuntimeException("Insufficient stock for one or more products");
        }
        
        // Update stock for each product
        for (OrderItem item : order.getItems()) {
            productService.updateStock(item.getProduct().getId(), -item.getQuantity());
        }
        
        // Save the order
        orderRepository.save(order);
    }
}
