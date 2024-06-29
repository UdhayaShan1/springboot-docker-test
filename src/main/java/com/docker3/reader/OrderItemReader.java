package com.docker3.reader;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import com.docker3.fieldsetmapper.OrderFieldSetMapper;
import com.docker3.model.Orders;
import com.docker3.repository.UserRepository;

@Component
public class OrderItemReader extends FlatFileItemReader<Orders> {
    public OrderItemReader(UserRepository userRepository) {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("orderId", "nameOfOrder", "descriptionOfOrder", "dateOfOrder", "username");
        DefaultLineMapper<Orders> lineMapper = new DefaultLineMapper<Orders>();
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new OrderFieldSetMapper(userRepository));
        setResource(new ClassPathResource("csv/orders.csv"));
        setLineMapper(lineMapper);
        setLinesToSkip(1);
    }
}
