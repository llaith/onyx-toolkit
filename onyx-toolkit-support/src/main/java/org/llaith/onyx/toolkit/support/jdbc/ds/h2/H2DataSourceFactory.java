/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.jdbc.ds.h2;

import org.h2.jdbcx.JdbcDataSource;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 *
 */
public class H2DataSourceFactory implements Supplier<DataSource> {

    private final String url;
    private final String user;
    private final String pass;

    public H2DataSourceFactory(final String url, final String user, final String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public DataSource get() {

        final JdbcDataSource ds = new JdbcDataSource();

        ds.setURL(this.url);
        ds.setUser(this.user);
        ds.setPassword(this.pass);

        return ds;


    }

}
