/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.fn;

import org.llaith.onyx.toolkit.util.reflection.InstanceUtil;

import java.util.function.Supplier;

/**
 *
 */
public class InstanceSupplier<T> implements Supplier<T> {

    public static <X> InstanceSupplier<X> of(Class<X> klass) {
        return new InstanceSupplier<>(klass);
    }

    final Class<T> klass;

    public InstanceSupplier(final Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public T get() {
        return InstanceUtil.newInstance(klass);
    }
    
}
