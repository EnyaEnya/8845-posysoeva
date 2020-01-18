package ru.cft.focusstart.repository.customer;

import ru.cft.focusstart.entity.Customer;
import ru.cft.focusstart.repository.AbstractRepository;
import ru.cft.focusstart.repository.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.cft.focusstart.repository.reader.CustomerReader.readCustomer;

public class JdbcCustomerRepository extends AbstractRepository<Customer> implements CustomerRepository {

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

    public static JdbcCustomerRepository getInstance() {
        return INSTANCE;
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
    protected String getAddQuery() {
        return ADD_QUERY;
    }

    @Override
    protected String getByIdQuery() {
        return GET_BY_ID_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected Collection<Customer> readEntityList(ResultSet resultSet) throws SQLException {
        return readCustomersList(resultSet);
    }

    @Override
    protected void prepareAddStatement(Customer customer, PreparedStatement preparedStatement) throws SQLException {
        prepareFieldsValues(customer, preparedStatement);
    }

    @Override
    protected void prepareUpdateStatement(Customer customer, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(5, customer.getId());
    }

    private void prepareFieldsValues(Customer customer, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, customer.getFirstName());
        preparedStatement.setString(2, customer.getLastName());
        preparedStatement.setString(3, customer.getEmail());
        preparedStatement.setString(4, customer.getPhone());
    }

    private Collection<Customer> readCustomersList(ResultSet rs) throws SQLException {
        List<Customer> result = new ArrayList<>();
        while (rs.next()) {
            Customer customer = readCustomer(rs);
            result.add(customer);
        }
        return result;
    }

}
