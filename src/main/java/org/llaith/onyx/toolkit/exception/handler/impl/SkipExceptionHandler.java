/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.handler.impl;

import org.llaith.onyx.toolkit.exception.handler.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 */
public class SkipExceptionHandler implements ExceptionHandler<Exception> {

    private static final Logger logger = LoggerFactory.getLogger(SkipExceptionHandler.class);

    private int skipped = 0;

    @Override
    public void exceptionCaught(final Exception e) {

        this.skipped++;

        logger.info("Skipping exception:", e);

    }

    public int skipped() {
        return this.skipped;
    }

}
