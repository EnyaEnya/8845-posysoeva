package ru.cft.focusstart.mapper;

import ru.cft.focusstart.api.dto.CustomerDto;
import ru.cft.focusstart.entity.Customer;

public class CustomerMapper {

    private static final CustomerMapper INSTANCE = new CustomerMapper();

    private CustomerMapper() {
    }

    public static CustomerMapper getInstance() {
        return INSTANCE;
    }

    public CustomerDto toDto(Customer customer) {
        return CustomerDto.builder()
                .id(customer.getId())
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .email(customer.getEmail())
                .phone(customer.getPhone())
                .build();
    }
}
