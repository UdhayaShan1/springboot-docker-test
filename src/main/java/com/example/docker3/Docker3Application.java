package com.example.docker3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
public class Docker3Application {

    @GetMapping
    public ResponseEntity<String> landingPage() {
        return ResponseEntity.status(HttpStatus.OK).body("Welcome");
    }

    public static void main(String[] args) {
        SpringApplication.run(Docker3Application.class, args);
    }

}
