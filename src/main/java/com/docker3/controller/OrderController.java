package com.docker3.controller;

import static com.docker3.util.Constants.DATE_TIME_FORMATTER;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.docker3.exception.order.OrderNotFoundException;
import com.docker3.model.Orders;
import com.docker3.model.Users;
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
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderService.getAllOrders());
    }

    @GetMapping("/{personId}/order")
    public ResponseEntity<List<Orders>> getAllOrdersByUser(@PathVariable Long personId) {
        Optional<Users> user = userRepository.findById(personId);
        return user.map(
                value -> ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdersByUser(value))).orElseGet(
                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ArrayList<>()));
    }


    @PostMapping("/addorder")
    public ResponseEntity<Orders> addOrder(@RequestBody Orders orders) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrder(orders));
    }

    @PostMapping("/addorder/{personId}")
    public ResponseEntity<Orders> addOrderWithPerson(@RequestBody Orders orders, @PathVariable Long personId) {
        Optional<Users> person = userRepository.findById(personId);
        if (person.isPresent()) {
            orders.setUsers(person.get());
            return ResponseEntity.status(HttpStatus.OK).body(orderService.addOrder(orders));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Actual web controllers

    @GetMapping("/getorders")
    public ResponseEntity<List<Orders>> getOrdersOfCurrentUser(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Users users = (Users) session.getAttribute("user");
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findAllOrdersByUser(users));
    }

    @PostMapping("/neworder")
    public ResponseEntity<Orders> userAddOrder(@RequestBody Orders orders, HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Users users = (Users) session.getAttribute("user");
        Users retrievedUsers = userRepository.findByUsername(users.getUsername());
        if (retrievedUsers == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        orders.setOrderId(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        orders.setUsers(retrievedUsers);
        orders.setDateOfOrder(LocalDate.now());
        orderService.addOrder(orders);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
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

    @GetMapping("/edit/displayorder")
    public ResponseEntity<Orders> getOrderBasedOnId(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (session.getAttribute("orderIdToEdit") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Long orderId = (Long) session.getAttribute("orderIdToEdit");
        try {
            Orders orders = orderService.getOrderBasedOnId(orderId);
            return ResponseEntity.status(HttpStatus.OK).body(orders);
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/edit/updateorder")
    public ResponseEntity<Void> updateOrderOnEditPage(@RequestBody Orders orders, HttpSession session) {
        System.out.println(session);
        if (session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if (session.getAttribute("orderIdToEdit") == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println(orders);
        try {
            orderService.updateOrderOnEditPage((Long) session.getAttribute("orderIdToEdit"), orders);
            session.removeAttribute("orderIdToEdit");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }



}
