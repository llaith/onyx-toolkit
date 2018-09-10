package org.llaith.onyx.toolkit.fn.impl;


import org.llaith.onyx.toolkit.lang.Guard;

import java.util.function.Consumer;

/**
 *
 */
public class CountingConsumer {

    private long counter;

    private long period;

    private final Consumer<Long> consumer;

    public CountingConsumer(final long start, final long period, Consumer<Long> consumer) {

        this.counter = start;

        this.period = period;

        this.consumer = Guard.notNull(consumer);

    }

    public void increment() {

        this.counter++;

        if (this.counter >= this.period) this.consumer.accept(this.counter);

    }

}
