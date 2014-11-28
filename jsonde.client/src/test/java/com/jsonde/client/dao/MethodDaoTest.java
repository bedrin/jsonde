package com.jsonde.client.dao;

import com.jsonde.client.domain.Method;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MethodDaoTest extends TestCase {

    private DataSource testDataSource;
    private MethodDao methodDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testDataSource =
                JdbcConnectionPool.create("jdbc:h2:target/test-database/db", "sa", "sa");
        methodDao =
                new MethodDao(testDataSource);

        methodDao.createTable();

    }

    @Override
    protected void tearDown() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("drop table " + methodDao.getTableName());
            preparedStatement.execute();
        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

    }

    public void testCreateTable() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("select * from " + methodDao.getTableName());
            preparedStatement.execute();
        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

    }

    public void testInsertMethod() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Method method = new Method();
        method.setId(1);
        method.setName("Class name");

        methodDao.insert(method);

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("select count(*) from " + methodDao.getTableName());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            resultSet.next();

            int count = resultSet.getInt(1);
            assertEquals(1, count);

        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

    }

    public void testGetMethod() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("insert into " + methodDao.getTableName() + " (id,name) values (?,?)");
            preparedStatement.setInt(1, 10);
            preparedStatement.setString(2, "test");
            preparedStatement.execute();
        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

        Method method = methodDao.get(10);

        assertEquals(10, method.getId());
        assertEquals("test", method.getName());

    }

}