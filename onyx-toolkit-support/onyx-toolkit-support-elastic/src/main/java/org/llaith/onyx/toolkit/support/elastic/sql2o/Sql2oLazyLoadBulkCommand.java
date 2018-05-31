/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.elastic.sql2o;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import org.sql2o.data.LazyTable;
import org.sql2o.data.Row;
import org.llaith.onyx.toolkit.support.elastic.ElasticBulkCommand;
import org.llaith.onyx.toolkit.support.core.sql2o.Sql2oUtil;
import org.llaith.onyx.toolkit.util.exception.ExceptionHandler;
import org.llaith.onyx.toolkit.util.exception.impl.RethrowExceptionHandler;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.Map;

/**
 *
 */
public class Sql2oLazyLoadBulkCommand implements ElasticBulkCommand {

    public interface RowProcessor {

        void process(BulkProcessor elastic, Row row);

    }

    private final Sql2o database;
    private final String sql;
    private final Map<String,Object> parameters;
    private final RowProcessor processor;
    private final ExceptionHandler<Exception> exceptionHandler;

    public Sql2oLazyLoadBulkCommand(final Sql2o database, final String sql, final Map<String,Object> params, final RowProcessor processor) {
        this(
                database,
                sql,
                params,
                processor,
                new RethrowExceptionHandler());
    }

    public Sql2oLazyLoadBulkCommand(final Sql2o database, final String sql, final Map<String,Object> params, final RowProcessor processor, final ExceptionHandler<Exception> exceptionHandler) {
        this.database = Guard.notNull(database);
        this.sql = Guard.notNull(sql);
        this.processor = Guard.notNull(processor);
        this.exceptionHandler = Guard.notNull(exceptionHandler);
        this.parameters = params;
    }

    @SuppressWarnings("squid:S134") // easier like this
    @Override
    public int process(final BulkProcessor elastic) {

        // for batched/fetched reads to work, the fetchSize must be set, and a
        // transaction in place. We set the fetchSize inside the Sql2o class 
        // with reflection and we allow the transaction to rollback automatically
        // when sql2o autocloses the connection.
        try (final Connection con = database.beginTransaction()) {

            final Query queryTemplate = con.createQuery(this.sql);
            parameters.entrySet().forEach(entry -> queryTemplate.addParameter(entry.getKey(), entry.getValue()));

            try (final Query query = Sql2oUtil.setFetchSize(queryTemplate, 1000)) {

                try (final LazyTable table = query.executeAndFetchTableLazy()) {

                    int count = 0;

                    for (final Row row : table.rows()) {

                        try {

                            this.processor.process(elastic, row);

                            count++;

                        } catch (Exception e) {

                            this.exceptionHandler.exceptionCaught(e);

                        }

                    }

                    return count;

                }

            }

        }

    }


}
