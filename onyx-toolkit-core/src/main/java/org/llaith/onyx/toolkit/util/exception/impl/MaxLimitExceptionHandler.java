/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.exception.impl;

import org.llaith.onyx.toolkit.util.exception.ExceptionHandler;

/**
 * Designed to be *shared* among many wrapped error handlers to implement a general max-errors functionality
 * across sources and sinks.
 */
public class MaxLimitExceptionHandler implements ExceptionHandler<Exception> {

    private final int max;
    private final ExceptionHandler<Exception> handler;

    private int current = 0;

    public MaxLimitExceptionHandler(final int max, final ExceptionHandler<Exception> handler) {
        this.max = max;
        this.handler = handler;
    }

    @Override
    public void exceptionCaught(Exception e) {
        this.current++;
        if (current > max) throw new MaxErrorsException(MaxErrorsException.MaxType.TOTAL, this.max);
        this.handler.exceptionCaught(e);
    }

}
