package ru.cft.focusstart.repository.reader;

import ru.cft.focusstart.entity.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class CustomerReader {

    private CustomerReader() {
    }

    public static Customer readCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setId(rs.getLong("id"));
        customer.setFirstName(rs.getString("first_name"));
        customer.setLastName(rs.getString("last_name"));
        customer.setEmail(rs.getString("email"));
        customer.setPhone(rs.getString("phone"));
        return customer;
    }
}
