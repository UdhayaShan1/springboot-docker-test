package com.docker3;

import java.util.UUID;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class Docker3Application implements CommandLineRunner {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job job;

    public static void main(String[] args) {
        SpringApplication.run(Docker3Application.class, args);
    }

    public void run(String... args) throws Exception {
        jobLauncher.run(job, new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters());
    }
}
