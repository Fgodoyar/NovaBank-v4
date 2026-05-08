package com.novabank.operation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.novabank.operation.customer")
public class OperationServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OperationServiceApplication.class, args);
    }
}