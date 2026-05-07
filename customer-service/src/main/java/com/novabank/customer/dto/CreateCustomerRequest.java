package com.novabank.customer.dto;

public record CreateCustomerRequest(
        String customerName,
        String lastName,
        String dni,
        String email,
        String phoneNumber) {}