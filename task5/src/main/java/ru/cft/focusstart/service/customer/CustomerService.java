package ru.cft.focusstart.service.customer;

import ru.cft.focusstart.api.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto create(CustomerDto customerDto);

    CustomerDto getById(Long id);

    List<CustomerDto> get(String firstName, String lastName);

    CustomerDto merge(Long id, CustomerDto customerDto);

    void delete(Long id);
}
