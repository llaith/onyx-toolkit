/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.io.console;

/**
 * Runtime exception for handling console errors.
 *
 * @author McDowell
 */
public class ConsoleException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public ConsoleException(Throwable t) {
        super(t);
    }

}