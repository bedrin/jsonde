package com.jsonde.client.dao;

import com.jsonde.client.domain.DomainObject;
import com.jsonde.util.db.DbUtils;

import javax.sql.DataSource;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEntityDao<T extends DomainObject> extends AbstractDao {

    private Class<? super DomainObject> domainObjectClass;
    private BeanInfo domainObjectClassBeanInfo;

    @SuppressWarnings("unchecked")
    protected AbstractEntityDao(DataSource dataSource) throws DaoException {

        super(dataSource);

        ParameterizedType genericSuperClass = (ParameterizedType)
                getClass().getGenericSuperclass();

        this.domainObjectClass = (Class<? super DomainObject>)
                genericSuperClass.getActualTypeArguments()[0];

        try {
            domainObjectClassBeanInfo = Introspector.getBeanInfo(domainObjectClass);
        } catch (IntrospectionException e) {
            throw new DaoException(e);
        }

    }

    protected String getTableName() {
        return domainObjectClass.getSimpleName().toUpperCase();
    }

    public void createTable() throws DaoException {

        StringBuilder createTableQueryBuilder = new StringBuilder();

        createTableQueryBuilder.
                append("CREATE TABLE ").
                append(getTableName()).
                append(" ( ID INT PRIMARY KEY ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            Class propertyType = propertyDescriptor.getPropertyType();

            String databaseType = guessDatabaseType(propertyType);

            createTableQueryBuilder.
                    append(", ").
                    append(propertyName.toUpperCase()).
                    append(" ").
                    append(databaseType);

        }

        createTableQueryBuilder.append(")");

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = connection();
            preparedStatement = connection.prepareStatement(createTableQueryBuilder.toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

    public void insert(T domainObject) throws DaoException {
        Connection connection = connection();
        try {
            insert(connection, domainObject);
        } finally {
            DbUtils.close(connection);
        }
    }

    public void insert(Collection<T> domainObjects) throws DaoException {
        Connection connection = connection();

        boolean autoCommit;

        try {
            autoCommit = connection.getAutoCommit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }

        try {
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = null;

            try {

                preparedStatement =
                        connection.prepareStatement(createInsertQuery());

                for (T domainObject : domainObjects) {
                    insertImpl(preparedStatement, domainObject);
                }

            } catch (SQLException e) {
                throw new DaoException(e);
            } catch (IllegalAccessException e) {
                throw new DaoException(e);
            } catch (InvocationTargetException e) {
                throw new DaoException(e);
            } finally {
                DbUtils.close(preparedStatement);
            }

            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new DaoException(e);
            }
        } finally {
            try {
                connection.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DbUtils.close(connection);
        }
    }

    public void update(T domainObject) throws DaoException {
        Connection connection = connection();
        try {
            update(connection, domainObject);
        } finally {
            DbUtils.close(connection);
        }
    }

    public void insert(Connection connection, T domainObject) throws DaoException {

        PreparedStatement preparedStatement = null;

        try {

            preparedStatement =
                    connection.prepareStatement(createInsertQuery());

            insertImpl(preparedStatement, domainObject);

        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        } catch (InvocationTargetException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(preparedStatement);
        }

    }

    private String createInsertQuery() {

        StringBuilder insertQueryBuilder = new StringBuilder();

        insertQueryBuilder.
                append("INSERT INTO ").
                append(getTableName()).
                append("( ID ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            insertQueryBuilder.
                    append(", ").
                    append(propertyName.toUpperCase());

        }

        insertQueryBuilder.
                append(" ) ").
                append(" VALUES ( ").
                append(" ? ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            insertQueryBuilder.
                    append(", ? ");

        }

        insertQueryBuilder.append(" ) ");

        return insertQueryBuilder.toString();
    }

    private void insertImpl(PreparedStatement preparedStatement, T domainObject) throws SQLException, IllegalAccessException, InvocationTargetException {
        int parameterIndex = 1;
        preparedStatement.setLong(parameterIndex, domainObject.getId());

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = propertyDescriptor.getReadMethod().invoke(domainObject);
            Class propertyType = propertyDescriptor.getPropertyType();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            parameterIndex++;

            guessParameterTypeAndSet(preparedStatement, parameterIndex, propertyValue, propertyType);

        }

        preparedStatement.execute();
    }

    public void update(Connection connection, T domainObject) throws DaoException {

        StringBuilder insertQueryBuilder = new StringBuilder();

        insertQueryBuilder.
                append("UPDATE ").
                append(getTableName()).
                append(" SET ").
                append("ID = ? ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            insertQueryBuilder.
                    append(", ").
                    append(propertyName.toUpperCase()).
                    append(" = ").
                    append(" ? ");

        }

        insertQueryBuilder.append(" WHERE ID = ? ");

        PreparedStatement preparedStatement = null;

        try {

            preparedStatement =
                    connection.prepareStatement(insertQueryBuilder.toString());

            int parameterIndex = 1;
            preparedStatement.setLong(parameterIndex, domainObject.getId());

            for (PropertyDescriptor propertyDescriptor :
                    domainObjectClassBeanInfo.getPropertyDescriptors()) {
                String propertyName = propertyDescriptor.getName();
                Object propertyValue = propertyDescriptor.getReadMethod().invoke(domainObject);
                Class propertyType = propertyDescriptor.getPropertyType();

                if ("id".equalsIgnoreCase(propertyName)) continue;
                if ("class".equalsIgnoreCase(propertyName)) continue;

                parameterIndex++;

                guessParameterTypeAndSet(preparedStatement, parameterIndex, propertyValue, propertyType);

            }

            parameterIndex++;

            preparedStatement.setLong(parameterIndex, domainObject.getId());

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        } catch (InvocationTargetException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(preparedStatement);
        }

    }

    public long count() throws DaoException {

        String query = "SELECT COUNT(*) FROM " + getTableName();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = connection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();

            resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                return resultSet.getLong(1);
            } else {
                throw new DaoException("Unable to get count of entries in table " + getTableName());
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

    public T get(long id) throws DaoException {

        T domainObject = createDomainObject();

        StringBuilder getQueryBuilder = new StringBuilder();

        getQueryBuilder.
                append("SELECT ").
                append("ID ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            getQueryBuilder.
                    append(", ").
                    append(propertyName.toUpperCase());

        }

        getQueryBuilder.
                append(" FROM ").
                append(getTableName()).
                append(" WHERE ID = ?");

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection =
                    connection();
            preparedStatement =
                    connection.prepareStatement(getQueryBuilder.toString());

            preparedStatement.setLong(1, id);

            preparedStatement.execute();

            resultSet =
                    preparedStatement.getResultSet();

            if (resultSet.next()) {

                domainObject.setId(id);

                int parameterIndex = 1;

                for (PropertyDescriptor propertyDescriptor :
                        domainObjectClassBeanInfo.getPropertyDescriptors()) {
                    String propertyName = propertyDescriptor.getName();
                    Class propertyType = propertyDescriptor.getPropertyType();

                    if ("id".equalsIgnoreCase(propertyName)) continue;
                    if ("class".equalsIgnoreCase(propertyName)) continue;

                    parameterIndex++;

                    Object propertyValue = resultSet.getObject(parameterIndex);

                    if (null != propertyValue) {

                        if (Boolean.class == propertyType || boolean.class == propertyType) {
                            Boolean booleanPropertyValue = ((Number) propertyValue).shortValue() == 1;
                            propertyDescriptor.getWriteMethod().invoke(domainObject, new Object[]{booleanPropertyValue});
                        } else {
                            propertyDescriptor.getWriteMethod().invoke(domainObject, new Object[]{propertyValue});
                        }


                    }

                }

            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        } catch (InvocationTargetException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

        return domainObject;

    }

    public T getUniqueByCondition(String condition, Object... parameters) throws DaoException {

        List<T> result = getByCondition(condition, parameters);

        if (1 < result.size()) {
            throw new DaoException("Not uique result");
        } else if (1 == result.size()) {
            return result.get(0);
        } else {
            return null;
        }

    }

    public List<T> getAll() throws DaoException {
        return getByCondition(null);
    }

    public List<T> getByCondition(String condition, Object... parameters) throws DaoException {

        List<T> resultList = new LinkedList<T>();

        StringBuilder getQueryBuilder = new StringBuilder();

        getQueryBuilder.
                append("SELECT ").
                append("ID ");

        for (PropertyDescriptor propertyDescriptor :
                domainObjectClassBeanInfo.getPropertyDescriptors()) {
            String propertyName = propertyDescriptor.getName();

            if ("id".equalsIgnoreCase(propertyName)) continue;
            if ("class".equalsIgnoreCase(propertyName)) continue;

            getQueryBuilder.
                    append(", ").
                    append(propertyName.toUpperCase());

        }

        getQueryBuilder.
                append(" FROM ").
                append(getTableName());

        if (null != condition) {
            getQueryBuilder.
                    append(" WHERE ").
                    append(condition);
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection =
                    connection();
            preparedStatement =
                    connection.prepareStatement(getQueryBuilder.toString());

            if (null != parameters) {

                for (int parameterIndex = 1; parameterIndex <= parameters.length; parameterIndex++) {
                    preparedStatement.setObject(parameterIndex, parameters[parameterIndex - 1]);
                }

            }

            preparedStatement.execute();

            resultSet =
                    preparedStatement.getResultSet();

            while (resultSet.next()) {

                T domainObject = createDomainObject();

                int parameterIndex = 1;
                domainObject.setId(((Number) resultSet.getObject(parameterIndex)).longValue());

                for (PropertyDescriptor propertyDescriptor :
                        domainObjectClassBeanInfo.getPropertyDescriptors()) {
                    String propertyName = propertyDescriptor.getName();
                    Class propertyType = propertyDescriptor.getPropertyType();

                    if ("class".equalsIgnoreCase(propertyName)) continue;
                    if ("id".equalsIgnoreCase(propertyName)) continue;

                    parameterIndex++;

                    Object propertyValue = resultSet.getObject(parameterIndex);

                    if (Boolean.class == propertyType || boolean.class == propertyType) {
                        Boolean booleanPropertyValue = ((Number) propertyValue).shortValue() == 1;
                        propertyDescriptor.getWriteMethod().invoke(domainObject, new Object[]{booleanPropertyValue});
                    } else {
                        propertyDescriptor.getWriteMethod().invoke(domainObject, new Object[]{propertyValue});
                    }

                }

                resultList.add(domainObject);

            }

        } catch (SQLException e) {
            throw new DaoException(e);
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        } catch (InvocationTargetException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

        return resultList;

    }

    @SuppressWarnings("unchecked")
    public T createDomainObject() throws DaoException {
        try {
            return (T) domainObjectClass.newInstance();
        } catch (InstantiationException e) {
            throw new DaoException(e);
        } catch (IllegalAccessException e) {
            throw new DaoException(e);
        }
    }

    private void guessParameterTypeAndSet(PreparedStatement preparedStatement, int parameterIndex, Object propertyValue, Class propertyType) throws SQLException {
        if (Boolean.class == propertyType || boolean.class == propertyType) {
            Boolean booleanPropertyValue = (Boolean) propertyValue;
            preparedStatement.setObject(parameterIndex, booleanPropertyValue ? 1 : 0);
        } else {
            preparedStatement.setObject(parameterIndex, propertyValue);
        }
    }

    private static String guessDatabaseType(Class propertyType) {
        String databaseType = "VARCHAR";

        if (long.class == propertyType || Long.class == propertyType) {
            databaseType = "BIGINT";
        } else if (boolean.class == propertyType || Boolean.class == propertyType) {
            databaseType = "TINYINT";
        } else if (int.class == propertyType || Integer.class == propertyType) {
            databaseType = "INT";
        } else if (String.class == propertyType) {
            databaseType = "VARCHAR";
        }
        return databaseType;
    }

}
