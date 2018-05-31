/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.exception.impl;

import org.llaith.onyx.toolkit.util.exception.ExceptionHandler;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

/**
 *
 */
public class RethrowExceptionHandler implements ExceptionHandler<Exception> {

    @Override
    public void exceptionCaught(final Exception e) {
        throw ExceptionUtil.wrapException(e);
    }

}
