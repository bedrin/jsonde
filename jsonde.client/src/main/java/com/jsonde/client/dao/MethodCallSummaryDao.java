package com.jsonde.client.dao;

import com.jsonde.api.methodCall.MethodCallSummaryDto;
import com.jsonde.client.domain.MethodCallSummary;
import com.jsonde.util.db.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class MethodCallSummaryDao extends AbstractEntityDao<MethodCallSummary> {

    public MethodCallSummaryDao(DataSource dataSource) throws DaoException {
        super(dataSource);
    }

    @Override
    public void createTable() throws DaoException {
        super.createTable();
        execute("CREATE INDEX METHODCALLSUMMARY_CALLERID_IDX ON METHODCALLSUMMARY (CALLERID);");
        execute("CREATE INDEX METHODCALLSUMMARY_METHODID_IDX ON METHODCALLSUMMARY (METHODID);");
    }

    public void processMethodCallSummaryDto(MethodCallSummaryDto methodCallSummaryDto)
            throws DaoException {

        Connection connection = null;

        try {

            connection = connection();

            processMethodCallSummaryDto(methodCallSummaryDto, null, connection);

            connection.commit();

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(connection);
        }

    }

    private AtomicLong idGenerator = new AtomicLong();

    public void processMethodCallSummaryDto(MethodCallSummaryDto methodCallSummaryDto, Long callerId, Connection connection)
            throws DaoException {

        MethodCallSummary summary =
                null == callerId ?
                        getUniqueByCondition("CALLERID IS NULL AND METHODID = ?", methodCallSummaryDto.methodId) :
                        getUniqueByCondition("CALLERID = ? AND METHODID = ?", callerId, methodCallSummaryDto.methodId);

        if (null == summary) {

            summary = new MethodCallSummary();

            summary.setId(idGenerator.getAndIncrement());
            summary.setCallerId(callerId);
            summary.setMethodId(methodCallSummaryDto.methodId);

            summary.executionTime = methodCallSummaryDto.executionTime;
            summary.throwExceptionCounter = Long.valueOf(methodCallSummaryDto.exceptionCount);
            summary.invocationCount = Long.valueOf(methodCallSummaryDto.invocationCount);

            insert(connection, summary);

        } else {
            summary.executionTime += methodCallSummaryDto.executionTime;
            summary.throwExceptionCounter += methodCallSummaryDto.exceptionCount;
            summary.invocationCount += methodCallSummaryDto.invocationCount;

            update(connection, summary);
        }

        for (MethodCallSummaryDto callee : methodCallSummaryDto.getCallees()) {
            processMethodCallSummaryDto(callee, summary.getId(), connection);
        }

    }

    public List<MethodCallSummary> getCpuProfilerData(Long callerId) throws DaoException {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = connection();

            if (null == callerId) {
                preparedStatement = connection.prepareStatement(
                        "select " +
                                "id, " +
                                "methodid, " +
                                "invocationcount, " +
                                "executiontime " +
                                "from methodcallsummary " +
                                "where callerid is null " +
                                "order by executiontime desc");
            } else {
                preparedStatement = connection.prepareStatement(
                        "select " +
                                "id, " +
                                "methodid, " +
                                "invocationcount, " +
                                "executiontime " +
                                "from methodcallsummary " +
                                "where callerid = ? " +
                                "order by executiontime desc");
                preparedStatement.setLong(1, callerId);
            }

            preparedStatement.execute();

            List<MethodCallSummary> methodCalls = new LinkedList<MethodCallSummary>();

            resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {

                long id = resultSet.getLong(1);
                long methodId = resultSet.getLong(2);
                long invocationCount = resultSet.getLong(3);
                long executionTime = resultSet.getLong(4);

                MethodCallSummary methodCall = new MethodCallSummary();

                methodCall.setId(id);
                methodCall.setMethodId(methodId);
                methodCall.setInvocationCount(invocationCount);
                methodCall.setExecutionTime(executionTime);

                methodCalls.add(methodCall);

            }

            return methodCalls;

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(resultSet);
            DbUtils.close(preparedStatement);
            DbUtils.close(connection);
        }

    }

}