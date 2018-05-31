/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.jdbc.ds.postgres;

import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 *
 */
public class PostgresDataSourceFactory implements Supplier<DataSource> {

    private final String url;
    private final String user;
    private final String pass;

    public PostgresDataSourceFactory(final String url, final String user, final String pass) {
        this.url = url;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public DataSource get() {

        final PGSimpleDataSource ds = new PGSimpleDataSource();

        ds.setUrl(this.url);
        ds.setUser(this.user);
        ds.setPassword(this.pass);

        return ds;

    }

}
