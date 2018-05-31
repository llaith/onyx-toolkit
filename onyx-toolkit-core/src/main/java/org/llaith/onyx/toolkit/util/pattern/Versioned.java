/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

/**
 *
 */
public interface Versioned<T> {

    public T target();

    public long version();

    public VersionedReference<T> newHandle();

}
