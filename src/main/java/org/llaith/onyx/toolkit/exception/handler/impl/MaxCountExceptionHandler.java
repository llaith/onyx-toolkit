/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.handler.impl;

import org.llaith.onyx.toolkit.exception.handler.ExceptionHandler;
import org.llaith.onyx.toolkit.lang.Guard;

/**
 * This wraps another ExceptionHandler and let's that handle the errors util it hits it's
 * max count, then it throws. Note that the underlying exception handler may still throw
 * an error anyway.
 */
public class MaxCountExceptionHandler implements ExceptionHandler<Exception> {

    private static final int MAX_DEFAULT = 3;

    private final int maxRepeatedErrors;
    private final ExceptionHandler<Exception> handler;

    private int currentRepeatedErrors = 0;

    public MaxCountExceptionHandler(final ExceptionHandler<Exception> handler) {
        this(MAX_DEFAULT, handler);
    }

    public MaxCountExceptionHandler(final int maxRepeatedErrors, final ExceptionHandler<Exception> handler) {
        this.maxRepeatedErrors = maxRepeatedErrors;
        this.handler = Guard.notNull(handler);
    }

    @Override
    public void exceptionCaught(final Exception e) {
        this.currentRepeatedErrors++;

        if (this.currentRepeatedErrors > this.maxRepeatedErrors) throw new MaxErrorsException(
                MaxErrorsException.MaxType.REPEATED,
                this.maxRepeatedErrors);

        this.handler.exceptionCaught(e);
    }

}
