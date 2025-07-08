package repository;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class AbstractRepository<Entity> implements GenericRepository<Entity> {
    protected BasicDataSource dataSource;

    public AbstractRepository(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

}
