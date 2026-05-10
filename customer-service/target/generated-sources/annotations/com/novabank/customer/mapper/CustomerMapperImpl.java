package com.novabank.customer.mapper;

import com.novabank.customer.domain.Customer;
import com.novabank.customer.dto.CustomerDTO;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-11T00:08:59+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class CustomerMapperImpl implements CustomerMapper {

    @Override
    public CustomerDTO toDTO(Customer customer) {
        if ( customer == null ) {
            return null;
        }

        CustomerDTO.CustomerDTOBuilder customerDTO = CustomerDTO.builder();

        customerDTO.customerId( customer.getCustomerId() );
        customerDTO.customerName( customer.getCustomerName() );
        customerDTO.lastName( customer.getLastName() );
        customerDTO.dni( customer.getDni() );
        customerDTO.email( customer.getEmail() );
        customerDTO.phoneNumber( customer.getPhoneNumber() );
        customerDTO.creationDate( customer.getCreationDate() );

        return customerDTO.build();
    }

    @Override
    public Customer toEntity(CustomerDTO customerDTO) {
        if ( customerDTO == null ) {
            return null;
        }

        Customer.CustomerBuilder customer = Customer.builder();

        customer.customerId( customerDTO.getCustomerId() );
        customer.customerName( customerDTO.getCustomerName() );
        customer.lastName( customerDTO.getLastName() );
        customer.dni( customerDTO.getDni() );
        customer.email( customerDTO.getEmail() );
        customer.phoneNumber( customerDTO.getPhoneNumber() );
        customer.creationDate( customerDTO.getCreationDate() );

        return customer.build();
    }
}
