/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.jdbc;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public final class JdbcUtil {

    /**
     * Cannot be instantiated
     */
    private JdbcUtil() {
        super();
    }

    /**
     * Turns a typical jdbc sql-statement into a 'named parameter' sql statement such
     * as is used by sql2o and other libraries. Note that the parameter are compatible
     * with the jdbcmapper library, although there is no direct dependency.
     *
     * @param sql an sql statement with positional placeholders '?'.
     * @param parameters a list of the parameter names
     * @return an sql statement with the '?' converted to the parameter names.
     */
    public static String toNamedParameters(final String sql, final List<String> parameters) {

        final StringBuilder buf = new StringBuilder();

        int index = 0;
        for (final char c : sql.toCharArray()) {
            if (c == '?') {
                buf.append(":")
                   .append(parameters.get(index++));
            } else {
                buf.append(c);
            }
        }

        return buf.toString();
    }

    /**
     * This one comes from the AbstractQueryRunner class.
     * <p/>
     * Throws a new exception with a more informative error message.
     *
     * @param cause  The original exception that will be chained to the new
     *               exception when it's rethrown.
     * @param sql    The query that was executing when the exception happened.
     * @param params The query replacement parameters; <code>null</code> is a valid
     *               value to pass in.
     * @throws SQLException if a database access error occurs
     */
    public static void rethrow(final SQLException cause, final String sql, final Object... params) throws SQLException {

        final String msg =
                (cause.getMessage() != null ? cause.getMessage() : "") +
                        " Query: " +
                        sql +
                        " Parameters: " +
                        (params != null ? Arrays.deepToString(params) : "[]");

        final SQLException e = new SQLException(msg, cause.getSQLState(), cause.getErrorCode());
        e.setNextException(cause);

        throw e;

    }

}

