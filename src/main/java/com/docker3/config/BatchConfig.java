package com.docker3.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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

    @Bean
    public Job importOrdersJob(JobRepository jobRepository, Step importOrdersStep) {
        return new JobBuilder("importOrdersJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importOrdersStep)
                .build();
    }

    @Bean
    public Step importOrdersStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                 OrderItemReader orderItemReader, OrderItemProcessor orderItemProcessor,
                                 OrderItemWriter orderItemWriter) {
        return new StepBuilder("importOrdersStep", jobRepository)
                .<Orders, Orders>chunk(10, transactionManager)
                .reader(orderItemReader)
                .processor(orderItemProcessor)
                .writer(orderItemWriter)
                .build();
    }

    @Bean
    public OrderItemReader orderItemReader(UserRepository userRepository) {
        return new OrderItemReader(userRepository);
    }

    @Bean
    public OrderItemProcessor orderItemProcessor() {
        return new OrderItemProcessor();
    }

    @Bean
    public OrderItemWriter orderItemWriter() {
        return new OrderItemWriter();
    }
}
