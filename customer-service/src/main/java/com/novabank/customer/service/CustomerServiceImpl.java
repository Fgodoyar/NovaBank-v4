package com.novabank.customer.service;

import com.novabank.customer.domain.Customer;
import com.novabank.customer.dto.CreateCustomerRequest;
import com.novabank.customer.dto.CustomerDTO;
import com.novabank.customer.exception.CustomerNotFoundException;
import com.novabank.customer.mapper.CustomerMapper;
import com.novabank.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    @Override
    public List<CustomerDTO> listCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerDTO findById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado: " + customerId));
        return customerMapper.toDTO(customer);
    }

    @Transactional
    @Override
    public CustomerDTO createCustomer(CreateCustomerRequest request) {

        if (customerRepository.existsByDni(request.dni()))
            throw new IllegalArgumentException("Ya existe un cliente con este DNI: " + request.dni());

        if (customerRepository.existsByEmail(request.email()))
            throw new IllegalArgumentException("Ya existe un cliente con este email: " + request.email());

        if (customerRepository.existsByPhoneNumber(request.phoneNumber()))
            throw new IllegalArgumentException("Ya existe un cliente con este número de teléfono: " + request.phoneNumber());

        Customer customer = Customer.builder()
                .customerName(request.customerName())
                .lastName(request.lastName())
                .dni(request.dni())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .creationDate(LocalDateTime.now())
                .build();

        Customer saved = customerRepository.saveAndFlush(customer);
        return customerMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    @Override
    public CustomerDTO findByDni(String dni) {
        Customer customer = customerRepository.findByDni(dni)
                .orElseThrow(() -> new CustomerNotFoundException("Cliente no encontrado: " + dni));
        return customerMapper.toDTO(customer);
    }

}