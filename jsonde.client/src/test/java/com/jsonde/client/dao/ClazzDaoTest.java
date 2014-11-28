package com.jsonde.client.dao;

import com.jsonde.client.domain.Clazz;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClazzDaoTest extends TestCase {

    private DataSource testDataSource;
    private ClazzDao clazzDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testDataSource =
                JdbcConnectionPool.create("jdbc:h2:target/test-database/db", "sa", "sa");
        clazzDao =
                new ClazzDao(testDataSource);

        clazzDao.createTable();

    }

    @Override
    protected void tearDown() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("drop table " + clazzDao.getTableName());
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
                    connection.prepareStatement("select * from " + clazzDao.getTableName());
            preparedStatement.execute();
        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

    }

    public void testInsertClazz() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Clazz clazz = new Clazz();
        clazz.setId(1);
        clazz.setName("Class name");
        clazz.setSuperClassId(null);

        clazzDao.insert(clazz);

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("select count(*) from " + clazzDao.getTableName());
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

    public void testGetAndUpdateClazz() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = testDataSource.getConnection();
            preparedStatement =
                    connection.prepareStatement("insert into " + clazzDao.getTableName() + " (id,name) values (?,?)");
            preparedStatement.setInt(1, 10);
            preparedStatement.setString(2, "test");
            preparedStatement.execute();
        } finally {
            if (null != preparedStatement)
                preparedStatement.close();
            if (null != connection)
                connection.close();
        }

        Clazz clazz = clazzDao.get(10);

        assertEquals(10, clazz.getId());
        assertEquals("test", clazz.getName());

        clazz.setName("updated");

        clazzDao.update(clazz);

        clazz = clazzDao.get(10);

        assertEquals(10, clazz.getId());
        assertEquals("updated", clazz.getName());

    }

    public void testGetNonExistClazz() throws Exception {

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        Clazz clazz = clazzDao.get(666);

        assertNull(clazz);

    }

}
