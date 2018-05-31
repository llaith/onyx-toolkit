/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.etc;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Primarily used for getting simply getting batches out of streams. If you know you
 * are working with batches, it's less trouble than using this class.
 */
public class Batch<T> implements Closeable {

    public interface FlushHandler<T> {
        void onFlush(Iterable<T> t) throws IOException;
    }

    private final int batchSize;
    private final FlushHandler<T> handler;

    private final List<T> records = new ArrayList<>();

    public Batch(final int batchSize, final FlushHandler<T> handler) {
        this.batchSize = batchSize;
        this.handler = handler;
    }

    public void add(final T t) throws IOException {
        this.records.add(t);
        if (this.records.size() > this.batchSize) this.flush();
    }

    public void flush() throws IOException {
        this.handler.onFlush(this.records);
        this.records.clear();
    }

    @Override
    public void close() throws IOException {
        if (!this.records.isEmpty()) this.flush();
    }

}
