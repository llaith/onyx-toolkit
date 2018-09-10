package org.llaith.onyx.toolkit.fn.impl;

import javax.annotation.Nullable;
import java.util.function.Predicate;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;

/**
 *
 */
public class AllowInstanceOf implements Predicate<Object> {

    public static AllowInstanceOf instanceOf(final Class<?> klass) {

        return new AllowInstanceOf(klass);

    }

    private final Class<?> klass;

    private AllowInstanceOf(final Class<?> klass) {

        this.klass = notNull(klass);

    }

    @Override
    public boolean test(final @Nullable Object o) {

        return klass.isInstance(o);

    }

}

