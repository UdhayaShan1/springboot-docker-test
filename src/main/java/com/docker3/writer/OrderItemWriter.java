package com.docker3.writer;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.docker3.model.Orders;
import com.docker3.repository.OrderRepository;
import com.docker3.service.OrderService;

@Component
public class OrderItemWriter implements ItemWriter<Orders> {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @Override
    public void write(Chunk<? extends Orders> chunk) throws Exception {
        for (Orders orders : chunk) {
            System.out.println("####");
            System.out.println(orders.getOrderId());
            orderService.updateOrderIfExistsElseInsertOrder(orders);
        }
    }
}
