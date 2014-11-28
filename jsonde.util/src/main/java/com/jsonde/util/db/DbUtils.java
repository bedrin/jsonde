package com.jsonde.util.db;

import com.jsonde.util.log.Log;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbUtils {

    private static final Log log = Log.getLog(DbUtils.class);

    public static void close(Connection connection) {

        final String METHOD_NAME = "close(Connection)";

        if (null == connection)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            log.error(METHOD_NAME, e);
        }

    }

    public static void close(Statement connection) {

        final String METHOD_NAME = "close(Statement)";

        if (null == connection)
            return;

        try {
            connection.close();
        } catch (SQLException e) {
            log.error(METHOD_NAME, e);
        }

    }

    public static void close(ResultSet resultSet) {

        final String METHOD_NAME = "close(ResultSet)";

        if (null == resultSet)
            return;

        try {
            resultSet.close();
        } catch (SQLException e) {
            log.error(METHOD_NAME, e);
        }

    }

}
