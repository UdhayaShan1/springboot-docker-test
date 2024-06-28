package com.docker3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docker3.model.Order;
import com.docker3.model.User;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user);
}
