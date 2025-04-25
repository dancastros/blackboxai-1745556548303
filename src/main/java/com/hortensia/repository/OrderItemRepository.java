package com.hortensia.repository;

import com.hortensia.model.Order;
import com.hortensia.model.OrderItem;
import com.hortensia.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrder(Order order);
    List<OrderItem> findByProduct(Product product);
    void deleteByOrder(Order order);
}
