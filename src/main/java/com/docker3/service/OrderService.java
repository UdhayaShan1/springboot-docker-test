package com.docker3.service;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.docker3.exception.order.OrderNotFoundException;
import com.docker3.model.Orders;
import com.docker3.model.Users;
import com.docker3.repository.OrderRepository;
import com.docker3.repository.UserRepository;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> findAllOrdersByUser(Users users) {
        return orderRepository.findAllByUsers(users);
    }

    public Orders addOrder(Orders orders) {
        return orderRepository.save(orders);
    }

    public void deleteOrder(Long id) throws OrderNotFoundException {
        if (orderRepository.findById(id).isEmpty()) {
            throw new OrderNotFoundException("Orders does not exist");
        }
        orderRepository.deleteById(id);
    }

    public void updateOrderIfExistsElseInsertOrder(Orders refOrders) {

        if (orderRepository.findByOrderId(refOrders.getOrderId()) == null) {
            orderRepository.save(refOrders);
        } else {
            Orders retrievedOrders = orderRepository.findByOrderId(refOrders.getOrderId());
            retrievedOrders.setNameOfOrder(refOrders.getNameOfOrder());
            retrievedOrders.setDescriptionOfOrder(refOrders.getDescriptionOfOrder());
            retrievedOrders.setDateOfOrder(refOrders.getDateOfOrder());
            orderRepository.save(retrievedOrders);
        }
    }

    public void runOrderBatchJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters());
    }

}
