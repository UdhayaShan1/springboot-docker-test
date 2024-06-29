package com.docker3.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.docker3.model.Orders;
import com.docker3.processor.OrderItemProcessor;
import com.docker3.reader.OrderItemReader;
import com.docker3.repository.UserRepository;
import com.docker3.writer.OrderItemWriter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    private OrderItemReader orderItemReader;
    @Autowired
    private OrderItemProcessor orderItemProcessor;
    @Autowired
    private OrderItemWriter orderItemWriter;
    @Autowired
    private UserRepository userRepository;


    @Bean
    public Job importOrdersJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importOrdersJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager, FlatFileItemReader<Orders> reader) {
        return new StepBuilder("step1", jobRepository)
                .<Orders, Orders>chunk(10, transactionManager)
                .reader(orderItemReader)
                .processor(orderItemProcessor)
                .writer(orderItemWriter)
                .build();
    }
}
