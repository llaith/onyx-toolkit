package org.llaith.onyx.toolkit.fn.lang;

/**
 *
 */
public interface TypedAutoCloseable<T extends Exception> extends AutoCloseable {

    @Override
    void close() throws T;
    
}
