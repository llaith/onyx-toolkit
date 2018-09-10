/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fn.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * Primarily used for getting simply getting batches out of streams. If you know you
 * are working with batches, it's less trouble than using this class.
 */
public class BatchConsumer<T> implements Consumer<T>, AutoCloseable {

    private final int batchSize;

    private final Consumer<Collection<T>> handler;

    private final List<T> records = new ArrayList<>();

    public BatchConsumer(final int batchSize, final Consumer<Collection<T>> handler) {

        this.batchSize = batchSize;

        this.handler = handler;

    }

    @Override
    public void accept(final T t) {

        this.records.add(t);

        if (this.records.size() > this.batchSize) this.flush();

    }

    public void flush() {

        this.handler.accept(this.records);

        this.records.clear();

    }

    @Override
    public void close() {

        if (!this.records.isEmpty()) this.flush();

    }

}
