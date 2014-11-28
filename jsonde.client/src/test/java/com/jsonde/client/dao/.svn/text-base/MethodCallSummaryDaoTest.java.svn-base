package com.jsonde.client.dao;

import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.api.methodCall.MethodCallSummaryDto;
import com.jsonde.client.domain.MethodCall;
import com.jsonde.client.domain.MethodCallSummary;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;

public class MethodCallSummaryDaoTest extends TestCase {

    private JdbcConnectionPool testDataSource;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testDataSource =
                JdbcConnectionPool.create("jdbc:h2:mem:testMethodCallSummary", "sa", "sa");

        DaoFactory.initialize(testDataSource);
        DaoFactory.createSchema();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        testDataSource.dispose();
    }

    public void testProcessMethodCallSummaryDto() throws Exception {

        MethodCallSummaryDao dao = DaoFactory.getMethodCallSummaryDao();

        MethodCallSummaryDto mcs1 = new MethodCallSummaryDto();
        mcs1.methodId = 1;
        mcs1.invocationCount = 1;

        MethodCallSummaryDto mcs2 = new MethodCallSummaryDto();
        mcs2.methodId = 2;
        mcs2.invocationCount = 2;
        
        mcs1.addCallee(mcs2);

        dao.processMethodCallSummaryDto(mcs1);
        dao.processMethodCallSummaryDto(mcs1);

        for (MethodCallSummary mcs : dao.getAll()) {
            System.out.println(mcs);
        }

    }


}