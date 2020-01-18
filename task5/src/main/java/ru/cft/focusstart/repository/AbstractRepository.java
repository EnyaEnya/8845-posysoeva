package ru.cft.focusstart.repository;

import ru.cft.focusstart.entity.AbstractEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.Collection;
import java.util.Optional;

public abstract class AbstractRepository <E extends AbstractEntity> implements EntityRepository<E> {

    protected final DataSource dataSource;

    protected AbstractRepository() {
        this.dataSource = DataSourceProvider.getDataSource();
    }

    protected abstract String getAddQuery();

    protected abstract String getByIdQuery();

    protected abstract String getDeleteQuery();

    protected abstract String getUpdateQuery();

    protected abstract void prepareAddStatement(E entity, PreparedStatement preparedStatement) throws SQLException;

    protected abstract void prepareUpdateStatement(E entity, PreparedStatement preparedStatement) throws SQLException;

    protected abstract Collection<E> readEntityList(ResultSet resultSet) throws SQLException;


    public Optional<E> getById(Long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(getByIdQuery())
        ) {
            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            Collection<E> entities = readEntityList(rs);

            if (entities.isEmpty()) {
                return Optional.empty();
            } else if (entities.size() == 1) {
                return Optional.of(entities.iterator().next());
            } else {
                throw new SQLException("Unexpected result set size");
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(getDeleteQuery())
        ) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void add(E entity) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(getAddQuery(), Statement.RETURN_GENERATED_KEYS)
        ) {
            prepareAddStatement(entity, ps);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            Long id = rs.next() ? rs.getLong(1) : null;
            if (id == null) {
                throw new SQLException("Unexpected error - could not obtain id");
            }
            entity.setId(id);
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public void update(E entity) {
        try (
                Connection con = dataSource.getConnection();
                PreparedStatement ps = con.prepareStatement(getUpdateQuery())
        ) {
            prepareUpdateStatement(entity, ps);

            ps.executeUpdate();
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }
}
