package com.docker3.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.docker3.model.Orders;
import com.docker3.model.Users;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findAllByUsers(Users users);
    Orders findByOrderId(String orderId);
}
