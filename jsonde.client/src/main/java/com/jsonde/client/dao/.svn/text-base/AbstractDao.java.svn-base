package com.jsonde.client.dao;

import com.jsonde.util.db.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AbstractDao {

    protected DataSource dataSource;

    public AbstractDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    protected Connection connection() throws DaoException {
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void execute(String sql) throws DaoException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

}
