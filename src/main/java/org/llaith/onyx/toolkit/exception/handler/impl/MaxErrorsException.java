/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.handler.impl;

/**
*
*/
public class MaxErrorsException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    public enum MaxType {
        TOTAL,REPEATED
    }

    public MaxErrorsException(final MaxType maxType, final int num) {
        super(String.format("The number of %s errors have passed maximum allowed amound (%d): ",maxType.name().toLowerCase(),num));
    }
}
