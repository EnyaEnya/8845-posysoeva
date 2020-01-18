package ru.cft.focusstart.service.customer;

import ru.cft.focusstart.api.dto.CustomerDto;
import ru.cft.focusstart.service.EntityService;

import java.util.List;

public interface CustomerService extends EntityService<CustomerDto> {

    CustomerDto create(CustomerDto customerDto);

    List<CustomerDto> get(String firstName, String lastName);

    CustomerDto merge(Long id, CustomerDto customerDto);

}
