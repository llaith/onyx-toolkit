package org.llaith.onyx.toolkit.support.core.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 */
public class DbStatement {

    private final PreparedStatement statement;

    public DbStatement(final PreparedStatement statement) {this.statement = statement;}

    public DbStatement setArray(final int parameterIndex, final Array x) throws SQLException {
        statement.setArray(parameterIndex, x);
        return this;
    }

    public DbStatement setAsciiStream(final int parameterIndex, final InputStream x) throws SQLException {
        statement.setAsciiStream(parameterIndex, x);
        return this;
    }

    public DbStatement setAsciiStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
        return this;
    }

    public DbStatement setAsciiStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        statement.setAsciiStream(parameterIndex, x, length);
        return this;
    }

    public DbStatement setBigDecimal(final int parameterIndex, final BigDecimal x) throws SQLException {
        statement.setBigDecimal(parameterIndex, x);
        return this;
    }

    public DbStatement setBinaryStream(final int parameterIndex, final InputStream x) throws SQLException {
        statement.setBinaryStream(parameterIndex, x);
        return this;
    }

    public DbStatement setBinaryStream(final int parameterIndex, final InputStream x, final int length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
        return this;
    }

    public DbStatement setBinaryStream(final int parameterIndex, final InputStream x, final long length) throws SQLException {
        statement.setBinaryStream(parameterIndex, x, length);
        return this;
    }

    public DbStatement setBlob(final int parameterIndex, final InputStream inputStream) throws SQLException {
        statement.setBlob(parameterIndex, inputStream);
        return this;
    }

    public DbStatement setBlob(final int parameterIndex, final InputStream inputStream, final long length) throws SQLException {
        statement.setBlob(parameterIndex, inputStream, length);
        return this;
    }

    public DbStatement setBlob(final int parameterIndex, final Blob x) throws SQLException {
        statement.setBlob(parameterIndex, x);
        return this;
    }

    public DbStatement setBoolean(final int parameterIndex, final boolean x) throws SQLException {
        statement.setBoolean(parameterIndex, x);
        return this;
    }

    public DbStatement setByte(final int parameterIndex, final byte x) throws SQLException {
        statement.setByte(parameterIndex, x);
        return this;
    }

    public DbStatement setBytes(final int parameterIndex, final byte[] x) throws SQLException {
        statement.setBytes(parameterIndex, x);
        return this;
    }

    public DbStatement setCharacterStream(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader);
        return this;
    }

    public DbStatement setCharacterStream(final int parameterIndex, final Reader reader, final int length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
        return this;
    }

    public DbStatement setCharacterStream(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setCharacterStream(parameterIndex, reader, length);
        return this;
    }

    public DbStatement setClob(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setClob(parameterIndex, reader);
        return this;
    }

    public DbStatement setClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setClob(parameterIndex, reader, length);
        return this;
    }

    public DbStatement setClob(final int parameterIndex, final Clob x) throws SQLException {
        statement.setClob(parameterIndex, x);
        return this;
    }

    public DbStatement setDate(final int parameterIndex, final Date x) throws SQLException {
        statement.setDate(parameterIndex, x);
        return this;
    }

    public DbStatement setDate(final int parameterIndex, final Date x, final Calendar cal) throws SQLException {
        statement.setDate(parameterIndex, x, cal);
        return this;
    }

    public DbStatement setDouble(final int parameterIndex, final double x) throws SQLException {
        statement.setDouble(parameterIndex, x);
        return this;
    }

    public DbStatement setFloat(final int parameterIndex, final float x) throws SQLException {
        statement.setFloat(parameterIndex, x);
        return this;
    }

    public DbStatement setInt(final int parameterIndex, final int x) throws SQLException {
        statement.setInt(parameterIndex, x);
        return this;
    }

    public DbStatement setLong(final int parameterIndex, final long x) throws SQLException {
        statement.setLong(parameterIndex, x);
        return this;
    }

    public DbStatement setNCharacterStream(final int parameterIndex, final Reader value) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value);
        return this;
    }

    public DbStatement setNCharacterStream(final int parameterIndex, final Reader value, final long length) throws SQLException {
        statement.setNCharacterStream(parameterIndex, value, length);
        return this;
    }

    public DbStatement setNClob(final int parameterIndex, final Reader reader) throws SQLException {
        statement.setNClob(parameterIndex, reader);
        return this;
    }

    public DbStatement setNClob(final int parameterIndex, final Reader reader, final long length) throws SQLException {
        statement.setNClob(parameterIndex, reader, length);
        return this;
    }

    public DbStatement setNClob(final int parameterIndex, final NClob value) throws SQLException {
        statement.setNClob(parameterIndex, value);
        return this;
    }

    public DbStatement setNString(final int parameterIndex, final String value) throws SQLException {
        statement.setNString(parameterIndex, value);
        return this;
    }

    public DbStatement setNull(final int parameterIndex, final int sqlType) throws SQLException {
        statement.setNull(parameterIndex, sqlType);
        return this;
    }

    public DbStatement setNull(final int parameterIndex, final int sqlType, final String typeName) throws SQLException {
        statement.setNull(parameterIndex, sqlType, typeName);
        return this;
    }

    public DbStatement setObject(final int parameterIndex, final Object x) throws SQLException {
        statement.setObject(parameterIndex, x);
        return this;
    }

    public DbStatement setObject(final int parameterIndex, final Object x, final int targetSqlType) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
        return this;
    }

    public DbStatement setObject(final int parameterIndex, final Object x, final int targetSqlType, final int scaleOrLength) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        return this;
    }

    public DbStatement setObject(final int parameterIndex, final Object x, final SQLType targetSqlType) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType);
        return this;
    }

    public DbStatement setObject(final int parameterIndex, final Object x, final SQLType targetSqlType, final int scaleOrLength) throws SQLException {
        statement.setObject(parameterIndex, x, targetSqlType, scaleOrLength);
        return this;
    }

    public DbStatement setRef(final int parameterIndex, final Ref x) throws SQLException {
        statement.setRef(parameterIndex, x);
        return this;
    }

    public DbStatement setRowId(final int parameterIndex, final RowId x) throws SQLException {
        statement.setRowId(parameterIndex, x);
        return this;
    }

    public DbStatement setShort(final int parameterIndex, final short x) throws SQLException {
        statement.setShort(parameterIndex, x);
        return this;
    }

    public DbStatement setSQLXML(final int parameterIndex, final SQLXML xmlObject) throws SQLException {
        statement.setSQLXML(parameterIndex, xmlObject);
        return this;
    }

    public DbStatement setString(final int parameterIndex, final String x) throws SQLException {
        statement.setString(parameterIndex, x);
        return this;
    }

    public DbStatement setTime(final int parameterIndex, final Time x) throws SQLException {
        statement.setTime(parameterIndex, x);
        return this;
    }

    public DbStatement setTime(final int parameterIndex, final Time x, final Calendar cal) throws SQLException {
        statement.setTime(parameterIndex, x, cal);
        return this;
    }

    public DbStatement setTimestamp(final int parameterIndex, final Timestamp x) throws SQLException {
        statement.setTimestamp(parameterIndex, x);
        return this;
    }

    public DbStatement setTimestamp(final int parameterIndex, final Timestamp x, final Calendar cal) throws SQLException {
        statement.setTimestamp(parameterIndex, x, cal);
        return this;
    }

    public DbStatement setURL(final int parameterIndex, final URL x) throws SQLException {
        statement.setURL(parameterIndex, x);
        return this;
    }

    public PreparedStatement delegate() {
        return statement;
    }

}
