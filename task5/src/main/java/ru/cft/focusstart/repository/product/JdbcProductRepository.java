package ru.cft.focusstart.repository.product;

import ru.cft.focusstart.entity.Product;
import ru.cft.focusstart.repository.DataAccessException;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ru.cft.focusstart.repository.reader.ProductReader.readProduct;

public class JdbcProductRepository implements ProductRepository {

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

    private final DataSource dataSource;

    private JdbcProductRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    public static ProductRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public void add(Product product) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(ADD_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, product.getTitle());
            ps.setString(2, product.getDescription());
            ps.setLong(3, product.getPrice());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            product.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<Product> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<Product> products = readProductsList(rs);

            if (products.isEmpty()) {
                return Optional.empty();
            } else if (products.size() == 1) {
                return Optional.of(products.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
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
    public void update(Product product) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setString(1, product.getTitle());
            ps.setString(2, product.getDescription());
            ps.setLong(3, product.getPrice());
            ps.setLong(4, product.getId());

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(Product product) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(DELETE_QUERY)
        ) {
            ps.setLong(1, product.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
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
