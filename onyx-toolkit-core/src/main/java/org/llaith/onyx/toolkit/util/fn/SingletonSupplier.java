/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.fn;

import java.util.function.Supplier;

/**
 *
 */
public class SingletonSupplier<T> implements Supplier<T> {

    public static <X> SingletonSupplier<X> of(X instance) {
        return new SingletonSupplier<>(instance);
    }
    
    private final T singleton;

    public SingletonSupplier(final T singleton) {
        this.singleton = singleton;
    }

    @Override
    public T get() {
        return this.singleton;
    }

}
