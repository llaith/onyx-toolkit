/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.core.jdbc.ds.hikari;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.util.function.Supplier;

/**
 *
 */
public class HikariConnectionPoolFactory implements Supplier<DataSource> {

    private final DataSource dataSource;
    private final int poolSize;
    private final int idleTimeout;
    private final int leakDetectionThreshold;

    public HikariConnectionPoolFactory(final DataSource dataSource, final int poolSize, final int idleTimeout, final int leakDetectionThreshold) {
        this.dataSource = dataSource;
        this.poolSize = poolSize;
        this.idleTimeout = idleTimeout;
        this.leakDetectionThreshold = leakDetectionThreshold;
    }

    @Override
    public DataSource get() {

        final HikariConfig cfg = new HikariConfig();

        cfg.setDataSource(this.dataSource);

        cfg.setMaximumPoolSize(this.poolSize);

        cfg.setIdleTimeout(this.idleTimeout);

        cfg.setLeakDetectionThreshold(this.leakDetectionThreshold);

        return new HikariDataSource(cfg);

    }

}
