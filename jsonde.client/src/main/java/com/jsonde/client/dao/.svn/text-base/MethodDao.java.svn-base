package com.jsonde.client.dao;

import com.jsonde.client.domain.Method;

import javax.sql.DataSource;

public class MethodDao extends AbstractEntityDao<Method> {

    public MethodDao(DataSource dataSource) throws DaoException {
        super(dataSource);
    }

    @Override
    public void createTable() throws DaoException {
        super.createTable();
        execute("CREATE INDEX METHOD_CLASSID_IDX ON METHOD (CLASSID);");
    }
}