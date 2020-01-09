package ru.cft.focusstart.repository.customer;

import ru.cft.focusstart.entity.Customer;
import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.repository.DataAccessException;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.cft.focusstart.repository.reader.CustomerReader.readCustomer;

public class JdbcCustomerRepository implements CustomerRepository {

    private static final String ADD_QUERY =
            "insert into customer (first_name, last_name, email, phone) " +
                    "values (?, ?, ?, ?)";

    private static final String GET_QUERY =
            "select c.id          id," +
                    "       c.first_name        first_name," +
                    "       c.last_name last_name," +
                    "       c.email        email," +
                    "       c.phone          phone " +
                    "from customer c " +
                    "where true";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " and c.id = ?";

    private static final String GET_BY_NAME_QUERY =
            GET_QUERY +
                    " and lower(c.first_name) like lower('%' || ? || '%')" +
                    " and lower(c.last_name) like lower('%' || ? || '%')";

    private static final String UPDATE_QUERY =
            "update customer " +
                    "set first_name = ?, last_name = ?, email = ?, phone = ? " +
                    "where id = ?";

    private static final String DELETE_QUERY =
            "delete from customer where id = ?";

    private static final JdbcCustomerRepository INSTANCE = new JdbcCustomerRepository();

    private final DataSource dataSource;

    private JdbcCustomerRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static JdbcCustomerRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Customer customer) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            customer.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<Customer> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<Customer> customers = readCustomersList(rs);

            if (customers.isEmpty()) {
                return Optional.empty();
            } else if (customers.size() == 1) {
                return Optional.of(customers.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Customer> get(String firstName, String lastName) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_NAME_QUERY)
        ) {
            ps.setString(1, firstName == null ? "" : firstName);
            ps.setString(2, lastName == null ? "" : lastName);

            ResultSet rs = ps.executeQuery();

            Collection<Customer> customers = readCustomersList(rs);

            return new ArrayList<>(customers);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(Customer customer) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, customer.getFirstName());
            ps.setString(2, customer.getLastName());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getPhone());
            ps.setLong(5, customer.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(Customer customer) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_QUERY)
        ) {
            ps.setLong(1, customer.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Customer> readCustomersList(ResultSet rs) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (rs.next()) {
            Customer customer = readCustomer(rs);
            result.add(customer);
        }
        return result;
    }

    private Collection<Order> readOrdersList(ResultSet rs) throws SQLException {
        return null;
    }
}
