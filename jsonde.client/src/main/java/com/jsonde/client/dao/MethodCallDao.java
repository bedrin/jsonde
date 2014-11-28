package com.jsonde.client.dao;

import com.jsonde.api.methodCall.MethodCallDto;
import com.jsonde.client.domain.MethodCall;
import com.jsonde.util.db.DbUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class MethodCallDao extends AbstractEntityDao<MethodCall> {

    public MethodCallDao(DataSource dataSource) throws DaoException {
        super(dataSource);
    }

    @Override
    public void createTable() throws DaoException {
        super.createTable();
        execute("CREATE INDEX METHODCALL_CALLERID_IDX ON METHODCALL (CALLERID);");
        execute("CREATE INDEX METHODCALL_METHODID_IDX ON METHODCALL (METHODID);");
    }

    public MethodCall persistMethodCallDtos(
            MethodCallDto[] methodCallDtos) throws DaoException {

        long topMethodCallId = -1;

        Connection connection = connection();
        PreparedStatement insertMethodCallPreparedStatement = null;

        try {

            insertMethodCallPreparedStatement = connection.prepareStatement(
                    "INSERT INTO METHODCALL (ID, CALLERID, METHODID, ACTUALCLASSID) VALUES (?, ?, ?, ?)"
            );

            for (int i = 0; i < methodCallDtos.length; i++) {

                MethodCallDto methodCallDto = methodCallDtos[i];

                insertMethodCallPreparedStatement.setLong(1, methodCallDto.methodCallId);

                if ((1 << MethodCallDto.CALLER_ID_SET_FLAG) ==
                        (methodCallDto.flags & (1 << MethodCallDto.CALLER_ID_SET_FLAG))) {
                    insertMethodCallPreparedStatement.setLong(2, methodCallDto.callerId);
                } else {
                    insertMethodCallPreparedStatement.setNull(2, Types.BIGINT);
                }

                insertMethodCallPreparedStatement.setLong(3, methodCallDto.methodId);

                if ((1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG) ==
                        (methodCallDto.flags & (1 << MethodCallDto.ACTUAL_CLASS_ID_SET_FLAG))) {
                    insertMethodCallPreparedStatement.setLong(4, methodCallDto.actualClassId);
                } else {
                    insertMethodCallPreparedStatement.setNull(4, Types.BIGINT);
                }

                insertMethodCallPreparedStatement.addBatch();

                if (i == methodCallDtos.length - 1) {
                    topMethodCallId = methodCallDto.methodCallId;
                }

                methodCallDto.returnToPool();
                methodCallDtos[i] = null;

            }

            insertMethodCallPreparedStatement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            DbUtils.close(insertMethodCallPreparedStatement);
            DbUtils.close(connection);
        }

        return get(topMethodCallId);

    }

}