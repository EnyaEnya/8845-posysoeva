package ru.cft.focusstart.repository.customer;

import ru.cft.focusstart.entity.Customer;
import ru.cft.focusstart.repository.EntityRepository;

import java.util.List;

public interface CustomerRepository extends EntityRepository<Customer> {

    List<Customer> get(String firstName, String lastName);

}
