package com.novabank.customer.service;

import com.novabank.customer.dto.CreateCustomerRequest;
import com.novabank.customer.dto.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    List<CustomerDTO> listCustomers();
    CustomerDTO findById(Long customerId);
    CustomerDTO createCustomer(CreateCustomerRequest request);
    CustomerDTO findByDni(String dni);
}
