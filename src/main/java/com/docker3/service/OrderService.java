package com.docker3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.order.OrderNotFoundException;
import com.docker3.model.Order;
import com.docker3.model.User;
import com.docker3.repository.OrderRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> findAllOrdersByUser(User user) {
        return orderRepository.findAllByUser(user);
    }

    public Order addOrder(Order order) {
        return orderRepository.save(order);
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        if (orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Order does not exist");
        }
        orderRepository.deleteById(id);
    }

}
