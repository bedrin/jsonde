package com.jsonde.client.dao;

import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.client.domain.MethodCall;
import com.jsonde.client.domain.MethodCallSummary;
import junit.framework.TestCase;
import org.h2.jdbcx.JdbcConnectionPool;

public class MethodCallDaoTest extends TestCase {

    private JdbcConnectionPool testDataSource;
    private MethodCallDao methodCallDao;
    private MethodCallSummaryDao methodCallSummaryDao;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        testDataSource =
                JdbcConnectionPool.create("jdbc:h2:mem:test1", "sa", "sa");

        methodCallDao =
                new MethodCallDao(testDataSource);

        methodCallDao.createTable();

        methodCallSummaryDao =
                new MethodCallSummaryDao(testDataSource);

        methodCallSummaryDao.createTable();

    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        testDataSource.dispose();
    }

    public void testPersistMethodCallDtos() throws Exception {

        MethodCallDto m1 = createMethodCallDto(null, 100);
        MethodCallDto m1_1 = createMethodCallDto(m1.methodCallId, 50);
        MethodCallDto m1_1_1 = createMethodCallDto(m1_1.methodCallId, 20);
        MethodCallDto m1_1_2 = createMethodCallDto(m1_1.methodCallId, 30);
        MethodCallDto m1_2 = createMethodCallDto(m1.methodCallId, 50);

        methodCallDao.persistMethodCallDtos(
                new MethodCallDto[]{m1_1_1, m1_1_2}
        );

        methodCallDao.persistMethodCallDtos(
                new MethodCallDto[]{m1_1, m1_2, m1}
        );

        for (MethodCall methodCall : methodCallDao.getAll()) {
            System.out.println(methodCall);
        }

        for (MethodCallSummary methodCallSummary : methodCallSummaryDao.getAll()) {
            System.out.println(methodCallSummary);
        }

    }

    private long methodCallId;
    private long methodId;

    private MethodCallDto createMethodCallDto(Long callerId, long executionTime) {
        MethodCallDto m = new MethodCallDto();

        m.methodCallId = methodCallId++;
        m.methodId = methodId++;

        if (null != callerId) {
            m.flags |= 1 << MethodCallDto.CALLER_ID_SET_FLAG;
            m.callerId = callerId;
        }

        m.executionTime = executionTime;

        return m;
    }

}