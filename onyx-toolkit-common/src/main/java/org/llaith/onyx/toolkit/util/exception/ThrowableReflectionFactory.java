/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.exception;

import net.vidageek.mirror.dsl.Mirror;
import org.llaith.onyx.toolkit.util.exception.ExceptionUtil;

/**
 * NOTE: it's unlikely we want to keep this class. It's excessively complex now we have lambdas!
 * 
 * See RuntimeExceptionFactory for an alternative.
 * 
 * >>>
 * 
 * http://code.google.com/p/guava-libraries/wiki/ReflectionExplained
 *
 * Note, l10n of messages is best done outside the exception (the l10n message is passed to the ctor).
 * http://www.javaworld.com/article/2075897/testing-debugging/exceptional-practices--part-3.html
 */
public class ThrowableReflectionFactory<T extends Throwable> {

    private static final int DEFAULT_LEVEL = 8;

    private final Class<T> throwableClass;

    public ThrowableReflectionFactory(final Class<T> throwableClass) {
        this.throwableClass = throwableClass;
    }

    public T throwableFor(final String msg) {
        return throwableFor(DEFAULT_LEVEL+1,msg);
    }

    public T throwableFor(final int level, final String msg) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(this.throwableClass)
                .invoke()
                .constructor()
                .withArgs(msg));
    }

    public T throwableFor(final Throwable t) {
        return throwableFor(DEFAULT_LEVEL+1,t);
    }

    public T throwableFor(final int level, final Throwable t) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(this.throwableClass)
                .invoke()
                .constructor()
                .withArgs(t));
    }

    public T throwableFor(final String msg, final Throwable t) {
        return throwableFor(DEFAULT_LEVEL+1,msg,t);
    }

    public T throwableFor(final int level, final String msg, final Throwable t) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(this.throwableClass)
                .invoke()
                .constructor()
                .withArgs(msg, t));
    }

}
