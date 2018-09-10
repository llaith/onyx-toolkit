/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fn.lang;

/**
 *
 */
public interface TypedClosable<T extends Exception> extends AutoCloseable {

    @Override
    void close() throws T;
        
}
