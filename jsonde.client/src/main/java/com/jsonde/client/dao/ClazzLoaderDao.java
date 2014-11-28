package com.jsonde.client.dao;

import com.jsonde.client.domain.ClazzLoader;

import javax.sql.DataSource;

public class ClazzLoaderDao extends AbstractEntityDao<ClazzLoader> {

    public ClazzLoaderDao(DataSource dataSource) throws DaoException {
        super(dataSource);
    }

}