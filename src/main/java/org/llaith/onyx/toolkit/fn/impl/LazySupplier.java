/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fn.impl;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * From here:
 * https://github.com/spullara/java-future-jdk8/blob/master/src/main/java/spullara/util/Lazy.java
 * TODO: Verify it works as expected.
 * <p>
 * >>>>>>>
 * <p>
 * Value isn't set until you ask for it and is only
 * calculated once.
 * <p>
 * See
 * http://www.cs.umd.edu/~pugh/java/memoryModel/DoubleCheckedLocking.html
 * <p>
 * The section:
 * <p>
 * "Fixing Double-Checked Locking using Volatile"
 * <p>
 * This is also the same pattern that "lazy val" in Scala uses to
 * ensure once-and-only-once delayed initialization.
 */
public class LazySupplier<T> implements Supplier<T> {

    private volatile boolean set;
    private final Callable<T> callable;
    private T value;

    public static <T> LazySupplier<T> lazy(Callable<T> callable) {
        return new LazySupplier<>(callable);
    }

    private LazySupplier(Callable<T> callable) {
        this.callable = callable;
    }

    public T get() {
        // This access of set requires a memory barrier
        if (!set) {
            // Now we synchronize to have only a single executor
            synchronized (this) {
                // Check again to make sure another thread didn't beat us to the lock
                if (!set) {
                    // We got this.
                    try {
                        // Evaluate the passed lambda
                        value = callable.call();
                        set = true;
                    } catch (Exception e) {
                        throw new RuntimeException("LazySupplier initialization failure", e);
                    }
                }
            }
        }
        return value;
    }

}
