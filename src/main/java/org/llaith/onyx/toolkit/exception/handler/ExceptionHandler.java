/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.handler;

/**
 *
 */
public interface ExceptionHandler<E extends Exception> {

    void exceptionCaught(E e) throws RuntimeException;

}
