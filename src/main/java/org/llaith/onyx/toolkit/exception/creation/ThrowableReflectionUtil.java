/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception.creation;

import net.vidageek.mirror.dsl.Mirror;
import org.llaith.onyx.toolkit.exception.ExceptionUtil;

/**
 * NOTE: it's unlikely we want to keep this class. It's excessively complex now we have lambdas!
 * <p>
 * See RuntimeExceptionFactory for an alternative.
 * <p>
 * >>>
 * <p>
 * http://code.google.com/p/guava-libraries/wiki/ReflectionExplained
 * <p>
 * Note, l10n of messages is best done outside the exception (the l10n message is passed to the ctor).
 * http://www.javaworld.com/article/2075897/testing-debugging/exceptional-practices--part-3.html
 */
public class ThrowableReflectionUtil {

    private static final int DEFAULT_LEVEL = 8;

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final String msg) {
        return throwableFor(klass, DEFAULT_LEVEL + 1, msg);
    }

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final int level, final String msg) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(klass)
                .invoke()
                .constructor()
                .withArgs(msg));
    }

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final Throwable t) {
        return throwableFor(klass, DEFAULT_LEVEL + 1, t);
    }

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final int level, final Throwable t) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(klass)
                .invoke()
                .constructor()
                .withArgs(t));
    }

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final String msg, final Throwable t) {
        return throwableFor(klass, DEFAULT_LEVEL + 1, msg, t);
    }

    public static <T extends Throwable> T throwableFor(final Class<T> klass, final int level, final String msg, final Throwable t) {
        return ExceptionUtil.popStackTrace(DEFAULT_LEVEL + level, new Mirror()
                .on(klass)
                .invoke()
                .constructor()
                .withArgs(msg, t));
    }

}
