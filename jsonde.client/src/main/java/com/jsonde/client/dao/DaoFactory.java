package com.jsonde.client.dao;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DaoFactory {

    private static DaoFactory instance;

    private final List<AbstractEntityDao> daoList = new LinkedList<AbstractEntityDao>();

    private DataSource dataSource;

    private final ClazzLoaderDao clazzLoaderDao;
    private final CodeSourceDao codeSourceDao;
    private final ClazzDao clazzDao;
    private final MethodDao methodDao;
    private final MethodCallDao methodCallDao;
    private final TopMethodCallDao topMethodCallDao;
    private final MethodCallSummaryDao methodCallSummaryDao;
    private final ReportDao reportDao;
    private final TelemetryDataDao telemetryDataDao;

    public static void initialize(DataSource dataSource) throws DaoException {
        instance = new DaoFactory(dataSource);
    }

    private DaoFactory(DataSource dataSource) throws DaoException {

        this.dataSource = dataSource;

        clazzLoaderDao = new ClazzLoaderDao(dataSource);
        codeSourceDao = new CodeSourceDao(dataSource);
        clazzDao = new ClazzDao(dataSource);
        methodDao = new MethodDao(dataSource);
        methodCallDao = new MethodCallDao(dataSource);
        topMethodCallDao = new TopMethodCallDao(dataSource);
        methodCallSummaryDao = new MethodCallSummaryDao(dataSource);
        reportDao = new ReportDao(dataSource);
        telemetryDataDao = new TelemetryDataDao(dataSource);

        daoList.add(clazzLoaderDao);
        daoList.add(codeSourceDao);
        daoList.add(clazzDao);
        daoList.add(methodDao);
        daoList.add(methodCallDao);
        daoList.add(topMethodCallDao);
        daoList.add(methodCallSummaryDao);
        daoList.add(telemetryDataDao);
    }

    public static ClazzLoaderDao getClazzLoaderDao() {
        return instance.clazzLoaderDao;
    }

    public static CodeSourceDao getCodeSourceDao() {
        return instance.codeSourceDao;
    }

    public static ClazzDao getClazzDao() {
        return instance.clazzDao;
    }

    public static MethodDao getMethodDao() {
        return instance.methodDao;
    }

    public static MethodCallDao getMethodCallDao() {
        return instance.methodCallDao;
    }

    public static TopMethodCallDao getTopMethodCallDao() {
        return instance.topMethodCallDao;
    }

    public static MethodCallSummaryDao getMethodCallSummaryDao() {
        return instance.methodCallSummaryDao;
    }

    public static ReportDao getReportDao() {
        return instance.reportDao;
    }

    public static TelemetryDataDao getTelemetryDataDao() {
        return instance.telemetryDataDao;
    }

    public static Connection getConnection() throws DaoException {
        try {
            return instance.dataSource.getConnection();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public static void createSchema() throws DaoException {
        for (AbstractEntityDao dao : instance.daoList) {
            dao.createTable();
        }
    }

}
