package com.novabank.operation.customer;

import com.novabank.operation.dto.CustomerDTO;
import org.springframework.stereotype.Component;

@Component
public class FallbackServiceClient implements CustomerServiceClient {

    @Override
    public CustomerDTO getCustomer(Long id) {
        return new CustomerDTO(id, "Cliente no disponible", "", "", "", "");
    }
}