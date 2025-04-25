package com.hortensia.repository;

import com.hortensia.model.Order;
import com.hortensia.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByOrderDateDesc(User user);
    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
