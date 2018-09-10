/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fn.lang;

/**
 *
 */
public interface AutoClosed extends TypedClosable<RuntimeException> {

    static AutoClosed of(final AutoClosed autoClosed) {
        return autoClosed;   
    }
    
}
