package org.llaith.onyx.toolkit.fn.impl;

import org.llaith.onyx.toolkit.fn.ExcecutionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;


/**
 *
 */
public class CompoundConsumer<T> implements Consumer<T> {

    public static class CompoundConsumerFactory<T> implements Supplier<Consumer<T>> {

        private final List<Consumer<T>> consumers = new ArrayList<>();

        private final Consumer<Exception> exceptionConsumer;

        public CompoundConsumerFactory() {
            this(null);
        }

        public CompoundConsumerFactory(final Consumer<Exception> exceptionConsumer) {
            this.exceptionConsumer = exceptionConsumer;
        }

        public CompoundConsumer<T> addConsumer(final Consumer<T> consumer) {

            this.consumers.add(notNull(consumer));

            return new CompoundConsumer<>(this);

        }

        @Override
        public Consumer<T> get() {
            return new CompoundConsumer<>(this);
        }

    }

    private final CompoundConsumerFactory<T> factory;

    private CompoundConsumer(final CompoundConsumerFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public void accept(final T t) {

        for (final Consumer<T> consumer : this.factory.consumers) {

            if (this.factory.exceptionConsumer != null) {

                ExcecutionUtil.suppressException(() -> consumer.accept(t), this.factory.exceptionConsumer::accept);

            } else {

                consumer.accept(t);

            }

        }

    }

}
