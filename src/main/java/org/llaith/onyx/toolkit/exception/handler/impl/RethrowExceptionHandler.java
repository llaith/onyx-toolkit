/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.handler.impl;

import org.llaith.onyx.toolkit.exception.ExceptionUtil;
import org.llaith.onyx.toolkit.exception.handler.ExceptionHandler;

/**
 *
 */
public class RethrowExceptionHandler implements ExceptionHandler<Exception> {

    @Override
    public void exceptionCaught(final Exception e) {
        throw ExceptionUtil.wrapException(e);
    }

}
