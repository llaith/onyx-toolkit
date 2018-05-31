/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.support.elastic;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

import java.util.concurrent.TimeUnit;

/**
 * http://rostislav-matl.blogspot.co.uk/2011/08/fast-inserts-to-postgresql-with-jdbc.html
 * <p>
 * <p>
 * https://github.com/google/guava/wiki/IOExplained
 */
public class ElasticBulkSession implements AutoCloseable {

    private final BulkProcessor processor;

    public ElasticBulkSession(final BulkProcessor processor) {
        this.processor = processor;
    }

    public int perform(final ElasticBulkCommand command) {

        return command.process(this.processor);

    }

    public boolean close(final int time, final TimeUnit timeUnit) throws InterruptedException {

        return this.processor.awaitClose(time, timeUnit);

    }

    public boolean forceClose(final int time, final TimeUnit timeUnit) {

        return ExceptionUtil.uninteruptableSupplier(() -> this.close(time, timeUnit));

    }

    @Override
    public void close() throws Exception {

        this.forceClose(1, TimeUnit.SECONDS);

    }

}
