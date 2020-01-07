package ru.cft.focusstart.repository.customer;

import ru.cft.focusstart.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    void add(Customer customer);

    Optional<Customer> getById(Long id);

    List<Customer> get(String firstName, String lastName);

    void update(Customer customer);

    void delete(Customer customer);
}
