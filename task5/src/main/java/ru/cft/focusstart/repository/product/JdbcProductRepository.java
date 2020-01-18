package ru.cft.focusstart.repository.product;

import ru.cft.focusstart.entity.Product;
import ru.cft.focusstart.repository.AbstractRepository;
import ru.cft.focusstart.repository.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.cft.focusstart.repository.reader.ProductReader.readProduct;

public class JdbcProductRepository extends AbstractRepository<Product> implements ProductRepository {

    private static final String ADD_QUERY =
            "insert into product (title, description, price) " +
                    "values (?, ?, ?)";

    private static final String GET_QUERY =
            "select p.id          product_id," +
                    "       p.title        product_title," +
                    "       p.description product_description," +
                    "       p.price          product_price " +
                    "from product p " +
                    "where true";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " and p.id = ?";

    private static final String GET_BY_TITLE_QUERY =
            GET_QUERY +
                    " and lower(p.title) like lower('%' || ? || '%')";

    private static final String UPDATE_QUERY =
            "update product " +
                    "set title = ?, description = ?, price = ? " +
                    "where id = ?";

    private static final String DELETE_QUERY =
            "delete from product where id = ?";


    private static final JdbcProductRepository INSTANCE = new JdbcProductRepository();

    public static ProductRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Product> get(String title) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_TITLE_QUERY)
        ) {
            ps.setString(1, title == null ? "" : title);
            ResultSet rs = ps.executeQuery();

            Collection<Product> customers = readProductsList(rs);

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
    protected Collection<Product> readEntityList(ResultSet resultSet) throws SQLException {
        return readProductsList(resultSet);
    }

    @Override
    protected void prepareAddStatement(Product product, PreparedStatement preparedStatement) throws SQLException {
        prepareFieldsValues(product, preparedStatement);
    }

    @Override
    protected void prepareUpdateStatement(Product product, PreparedStatement preparedStatement) throws SQLException {
        prepareFieldsValues(product, preparedStatement);
        preparedStatement.setLong(4, product.getId());
    }

    private void prepareFieldsValues(Product product, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, product.getTitle());
        preparedStatement.setString(2, product.getDescription());
        preparedStatement.setLong(3, product.getPrice());
    }


    private Collection<Product> readProductsList(ResultSet rs) throws SQLException {
        List<Product> result = new ArrayList<>();
        while (rs.next()) {
            Product product = readProduct(rs);
            result.add(product);
        }
        return result;
    }
}
