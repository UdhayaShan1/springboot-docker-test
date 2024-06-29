package com.docker3.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.sql.ast.tree.predicate.BooleanExpressionPredicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.docker3.exception.order.OrderNotFoundException;
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
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

    @GetMapping("/getorders")
    public ResponseEntity<List<Order>> getOrdersOfCurrentUser(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = (User) session.getAttribute("user");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdersByUser(user));
    }

    @PostMapping("/neworder")
    public ResponseEntity<Order> userAddOrder(@RequestBody Order order, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = (User) session.getAttribute("user");
        User retrievedUser = userRepository.findByUsername(user.getUsername());
        if (retrievedUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        order.setOrderId(LocalDateTime.now().format(formatter));
        order.setUser(retrievedUser);
        order.setDateOfOrder(LocalDate.now());
        orderService.addOrder(order);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(order);
    }

    @DeleteMapping("/deleteorder/{id}")
    public ResponseEntity<Void> deleteOrderBasedOnId(@PathVariable Long id, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }




}
