package org.llaith.onyx.toolkit.util.lang;

/**
 *
 */
public interface TypedAutoCloseable<T extends Exception> extends AutoCloseable {

    @Override
    void close() throws T;
    
}
