package ru.cft.focusstart.repository.order;

import ru.cft.focusstart.entity.Order;
import ru.cft.focusstart.entity.OrderItem;
import ru.cft.focusstart.repository.AbstractRepository;
import ru.cft.focusstart.repository.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static ru.cft.focusstart.repository.reader.OrderItemReader.readOrderItem;
import static ru.cft.focusstart.repository.reader.OrderReader.readOrder;

public class JdbcOrderRepository extends AbstractRepository<Order> implements OrderRepository {

    private static final JdbcOrderRepository INSTANCE = new JdbcOrderRepository();

    public static JdbcOrderRepository getInstance() {
        return INSTANCE;
    }

    private static final String INSERT_QUERY =
            "insert into order_table (customer_id) " +
                    "values (?)";

    private static final String UPDATE_QUERY =
            "update order_table " +
                    "set customer_id = ? " +
                    "where id = ?";


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
    protected String getByIdQuery() {
        return GET_BY_ID_QUERY;
    }

    @Override
    protected String getDeleteQuery() {
        return DELETE_ORDER_QUERY;
    }

    @Override
    protected String getUpdateQuery() {
        return UPDATE_QUERY;
    }

    @Override
    protected Collection<Order> readEntityList(ResultSet resultSet) throws SQLException {
        return readOrdersList(resultSet);
    }

    @Override
    protected String getAddQuery() {
        return INSERT_QUERY;
    }

    @Override
    protected void prepareAddStatement(Order order, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, order.getCustomerId());
    }

    @Override
    protected void prepareUpdateStatement(Order order, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setLong(1, order.getCustomerId());
        preparedStatement.setLong(2, order.getId());
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

            OrderItem orderItem = readOrderItem(rs);

            if (orderItem.getId() != 0) {
                orderItem.setOrderId(orderId);
                order.getOrderEntities().add(orderItem);
            }
        }
        return result.values();
    }
}
