package com.docker3.fieldsetmapper;

import static com.docker3.util.Constants.DATE_FORMATTER;

import java.time.LocalDate;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import com.docker3.model.Orders;
import com.docker3.model.Users;
import com.docker3.repository.UserRepository;

import lombok.NonNull;

public class OrderFieldSetMapper implements FieldSetMapper<Orders> {
    private final UserRepository userRepository;

    public OrderFieldSetMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Orders mapFieldSet(FieldSet fieldSet) throws BindException {
        Orders orders = new Orders();
        orders.setOrderId(fieldSet.readString("orderId").toLowerCase().trim());
        orders.setNameOfOrder(fieldSet.readString("nameOfOrder").toLowerCase().trim());
        orders.setDescriptionOfOrder(fieldSet.readString("descriptionOfOrder").toLowerCase().trim());
        orders.setDateOfOrder(LocalDate.parse(fieldSet.readString("dateOfOrder"), DATE_FORMATTER));
        setUserOfOrder(fieldSet, orders);
        return orders;
    }

    private void setUserOfOrder(FieldSet fieldSet, Orders orders) {
        String username = fieldSet.readString("username");
        System.out.println("Username: " + username);
        Users retrievedUsers = userRepository.findByUsername(username);
        if (retrievedUsers != null) {
            orders.setUsers(retrievedUsers);
        } else {
            Users users = new Users();
            users.setUsername("%ERROR%" + username);
            orders.setUsers(users);
        }
    }
}
