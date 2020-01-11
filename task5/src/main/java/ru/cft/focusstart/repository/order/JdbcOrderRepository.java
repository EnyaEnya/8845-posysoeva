package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderEntity;
import ru.cft.focusstart.repository.DataAccessException;
import ru.cft.focusstart.repository.DataSourceProvider;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

import static ru.cft.focusstart.repository.reader.OrderEntityReader.readOrderEntity;
import static ru.cft.focusstart.repository.reader.OrderReader.readOrder;

public class JdbcOrderRepository implements OrderRepository {

    private static final JdbcOrderRepository INSTANCE = new JdbcOrderRepository();

    public static JdbcOrderRepository getInstance() {
        return INSTANCE;
    }

    private final DataSource dataSource;

    private JdbcOrderRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    private static final String INSERT_QUERY =
            "insert into order_table (customer_id) " +
                    "values (?)";

    private static final String UPDATE_QUERY =
            "update order_table " +
                    "set customer_id = ? " +
                    "where id = ?";


    private static final String INSERT_ENTITIES_QUERY =
            "insert into order_entity (order_id, product_id, value) " +
                    "values (?, ?, ?)";

    private static final String UPDATE_ENTITIES_QUERY =
            "update order_entity " +
                    "set product_id = ?, value = ? " +
                    " where id = ?";

    private static final String GET_QUERY =
            "select o.id          order_id," +
                    "       o.customer_id        customer_id," +
                    "       e.id          entity_id," +
                    "       e.product_id        product_id," +
                    "       e.value product_value" +
                    "     from order_table o " +
                    "left join order_entity e on o.id = e.order_id";

    private static final String GET_BY_ID_QUERY =
            GET_QUERY +
                    " where o.id = ?";

    private static final String GET_BY_CUSTOMER_ID_QUERY =
            GET_QUERY +
                    " where o.customer_id = ?";

    private static final String DELETE_ORDER_QUERY =
            "delete from order_table where id = ?";

    private static final String DELETE_ENTITY_QUERY =
            "delete from order_entity where order_id = ?";


    @Override
    public void add(Order order) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT_QUERY, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setLong(1, order.getCustomerId());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            order.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Optional<Order> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<Order> orders = readOrdersList(rs);

            if (orders.isEmpty()) {
                return Optional.empty();
            } else if (orders.size() == 1) {
                return Optional.of(orders.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public List<Order> getByCustomerId(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(GET_BY_CUSTOMER_ID_QUERY)
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            return new ArrayList<>(readOrdersList(rs));

        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(Order order) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_QUERY)
        ) {
            ps.setLong(1, order.getCustomerId());
            ps.setLong(2, order.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void insertOrderEntity(OrderEntity orderEntity) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(INSERT_ENTITIES_QUERY, Statement.RETURN_GENERATED_KEYS);
        ) {
            ps.setLong(1, orderEntity.getOrderId());
            ps.setLong(2, orderEntity.getProductId());
            ps.setLong(3, orderEntity.getValue());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            orderEntity.setId(id);

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void updateOrderEntity(OrderEntity orderEntity) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(UPDATE_ENTITIES_QUERY);
        ) {
            ps.setLong(1, orderEntity.getProductId());
            ps.setLong(2, orderEntity.getValue());
            ps.setLong(3, orderEntity.getId());
            ps.executeUpdate();


        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(Order order) {
        delete(DELETE_ENTITY_QUERY, order.getId());
        delete(DELETE_ORDER_QUERY, order.getId());
    }

    private void delete(String sql, Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    private Collection<Order> readOrdersList(ResultSet rs) throws SQLException {
        Map<Long, Order> result = new HashMap<>();
        while (rs.next()) {
            long orderId = rs.getLong("order_id");

            Order order = result.get(orderId);
            if (order == null) {
                order = readOrder(rs);
                result.put(orderId, order);
            }

            if (rs.wasNull()) {
                continue;
            }

            OrderEntity orderEntity = readOrderEntity(rs);

            if (orderEntity.getId() != 0) {
                orderEntity.setOrderId(orderId);
                order.getOrderEntities().add(orderEntity);
            }
        }
        return result.values();
    }
}
