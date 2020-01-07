package ru.cft.focusstart.service.customer;

import ru.cft.focusstart.api.dto.CustomerDto;
import ru.cft.focusstart.entity.Customer;
import ru.cft.focusstart.exception.ObjectNotFoundException;
import ru.cft.focusstart.mapper.CustomerMapper;
import ru.cft.focusstart.repository.customer.CustomerRepository;
import ru.cft.focusstart.repository.customer.JdbcCustomerRepository;
import ru.cft.focusstart.repository.product.JdbcProductRepository;
import ru.cft.focusstart.repository.product.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.focusstart.service.validation.Validator.*;

public class DefaultCustomerService implements CustomerService {

    private static final DefaultCustomerService INSTANCE = new DefaultCustomerService();

    private final ProductRepository productRepository = JdbcProductRepository.getInstance();

    private final CustomerRepository customerRepository = JdbcCustomerRepository.getInstance();

    private final CustomerMapper customerMapper = CustomerMapper.getInstance();

    private DefaultCustomerService() {
    }

    public static CustomerService getInstance() {
        return INSTANCE;
    }

    @Override
    public CustomerDto create(CustomerDto customerDto) {
        validate(customerDto);

        Customer customer = add(null, customerDto);

        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerDto getById(Long id) {
        checkNotNull("id", id);

        Customer customer = getCustomer(id);

        return customerMapper.toDto(customer);
    }

    @Override
    public List<CustomerDto> get(String firstName, String lastName) {
        return customerRepository.get(firstName, lastName)
                .stream()
                .map(customerMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDto merge(Long id, CustomerDto customerDto) {
        checkNotNull("id", id);
        validate(customerDto);

        Customer customer = customerRepository.getById(id)
                .map(existing -> update(existing, customerDto))
                .orElseGet(() -> add(id, customerDto));

        return customerMapper.toDto(customer);
    }

    @Override
    public void delete(Long id) {
        checkNotNull("id", id);
        Customer customer = getCustomer(id);
        customerRepository.delete(customer);
    }

    private void validate(CustomerDto customerDto) {
        checkNull("customer.id", customerDto.getId());
        checkSize("customer.firstName", customerDto.getFirstName(), 1, 128);
        checkSize("customer.lastName", customerDto.getLastName(), 1, 128);
        checkSize("customer.email", customerDto.getEmail(), 1, 128);
        checkSize("customer.phone", customerDto.getPhone(), 1, 15);
    }

    private Customer add(Long id, CustomerDto customerDto) {

        Customer customer;

        if (id != null) {
            customer = getCustomer(id);
        } else {
            customer = new Customer();
        }
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());

        customerRepository.add(customer);
        return customer;
    }

    private Customer getCustomer(Long id) {
        return customerRepository.getById(id)
                .orElseThrow(() -> new ObjectNotFoundException(String.format("Customer with id %s not found", id)));
    }

    private Customer update(Customer customer, CustomerDto customerDto) {
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setEmail(customerDto.getEmail());
        customer.setPhone(customerDto.getPhone());
        customerRepository.update(customer);
        return customer;
    }
}
