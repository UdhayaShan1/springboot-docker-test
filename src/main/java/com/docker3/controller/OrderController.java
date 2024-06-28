package com.docker3.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.docker3.model.Order;
import com.docker3.model.User;
import com.docker3.repository.UserRepository;
import com.docker3.service.OrderService;

import jakarta.servlet.http.HttpSession;

@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/getallorders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAllOrders());
    }

    @GetMapping("/{personId}/order")
    public ResponseEntity<List<Order>> getAllOrdersByUser(@PathVariable Long personId) {
        Optional<User> user = userRepository.findById(personId);
        return user.map(
                value -> ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdersByUser(value))).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>()));
    }

    @GetMapping("/getorders")
    public ResponseEntity<List<Order>> getOrdersOfCurrentUser(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = (User) session.getAttribute("user");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdersByUser(user));
    }


    @PostMapping("/addorder")
    public ResponseEntity<Order> addOrder(@RequestBody Order order) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrder(order));
    }

    @PostMapping("/addorder/{personId}")
    public ResponseEntity<Order> addOrderWithPerson(@RequestBody Order order, @PathVariable Long personId) {
        Optional<User> person = userRepository.findById(personId);
        if (person.isPresent()) {
            order.setUser(person.get());
            return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrder(order));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }



}
