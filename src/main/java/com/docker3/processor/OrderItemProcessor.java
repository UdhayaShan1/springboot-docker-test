package com.docker3.processor;


import static com.docker3.util.Constants.DATE_TIME_FORMATTER;

import java.time.LocalDateTime;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.docker3.model.Orders;
import com.docker3.model.Users;

@Component
public class OrderItemProcessor implements ItemProcessor<Orders, Orders> {

    @Override
    public Orders process(Orders lineRecord) {
        System.out.println("###");
        if (invalidFields(lineRecord)) {
            return null;
        }
        if (isErrorUserFromReading(lineRecord.getUsers())) {
            System.out.println("Wrong username " + lineRecord.getUsers().getUsername());
            return null;
        }

        if (lineRecord.getOrderId() == null || lineRecord.getOrderId().isEmpty()) {
            lineRecord.setOrderId(LocalDateTime.now().format(DATE_TIME_FORMATTER));
        }
        System.out.println("######");
        System.out.println(lineRecord);
        return lineRecord;
    }

    private boolean invalidFields(Orders lineRecord) {
        return lineRecord.getNameOfOrder().isEmpty()
                || lineRecord.getDescriptionOfOrder().isEmpty()
                || (lineRecord.getDateOfOrder() == null);
    }

    private boolean isErrorUserFromReading(Users users) {
        return users.getUsername().contains("%ERROR%");
    }
}
