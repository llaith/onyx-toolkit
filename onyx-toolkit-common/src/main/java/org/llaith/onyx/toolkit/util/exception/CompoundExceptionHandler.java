package org.llaith.onyx.toolkit.util.exception;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CompoundExceptionHandler<E extends Exception> implements ExceptionHandler<E> {

    private final List<ExceptionHandler<E>> handlers = new ArrayList<>();

    public CompoundExceptionHandler<E> addHandler(final ExceptionHandler<E> handler) {

        this.handlers.add(handler);

        return this;

    }

    @Override
    public void exceptionCaught(final E e) {

        for (final ExceptionHandler<E> handler : this.handlers) {

            handler.exceptionCaught(e);

        }

    }
    
}
