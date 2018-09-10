/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.exception;

import org.llaith.onyx.toolkit.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.UUID;

import static org.llaith.onyx.toolkit.lang.Guard.notNull;

@SuppressWarnings("squid:S1200")
public class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    public static RuntimeException wrapException(final Throwable e) {
        if (e instanceof Error) throw (Error)e;
        if (e instanceof RuntimeException) return (RuntimeException)e;
        return new RuntimeException(e);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Exception> void throwUncheckedException(final Exception toThrow) throws T {
        throw (T)notNull(toThrow);
    }

    @SuppressWarnings("squid:S1872")
    public static <T extends Throwable> T findCause(final Class<T> klass, final Throwable t) {

        notNull(klass);

        Throwable srch = notNull(t);

        while (srch != null) {
            if (klass.getName()
                     .equals(srch.getClass()
                                 .getName())) return klass.cast(srch);
            srch = srch.getCause();
        }

        return null;

    }

    public static <T extends Throwable> T popStackTrace(final int trim, final T throwable) {

        final StackTraceElement[] originalTrace = throwable.getStackTrace();
        final StackTraceElement[] replacementTrace = new StackTraceElement[originalTrace.length - trim];

        System.arraycopy(originalTrace, trim, replacementTrace, 0, replacementTrace.length);

        throwable.setStackTrace(replacementTrace);

        return throwable;

    }

    public static String[] stackTraceToLines(final Throwable t) {

        return StringUtil.splitIntoLines(stackTraceToString(t));

    }

    public static String stackTraceToString(final Throwable t) {
        StringWriter out = new StringWriter();
        t.printStackTrace(new PrintWriter(out));
        return out.toString();
    }

    public static String stackTraceElementsToString(final StackTraceElement[] trace) {
        StringWriter sout = new StringWriter();
        PrintWriter pout = new PrintWriter(sout);

        for (StackTraceElement traceElement : trace)
            pout.println("\tat " + traceElement);

        return sout.toString();
    }

    @SuppressWarnings("unchecked")
    public static <E extends Exception> E findException(final Class<E> klass, final Exception e) {

        Throwable next = e;

        while (next != null) {

            if (klass.isAssignableFrom(next.getClass())) return (E)next;

            next = e.getCause();

        }

        return null;

    }

    @SuppressWarnings("squid:S106")
    public static void outputSqlException(final SQLException e) {

        outputSqlException(e, System.err);

    }

    public static void outputSqlException(final SQLException e, final PrintStream out) {

        SQLException next = e;

        while (next != null) {

            next.printStackTrace(out);

            next = next.getNextException();

        }

    }

    public static String prependExceptionId(final String mesg) {

        return String.format("(exception-id: %s) %s",
                             UUID.randomUUID().toString(),
                             mesg);

    }

    public static String appendExceptionId(final String mesg) {

        return String.format("%s (exception-id: %s)",
                             mesg,
                             UUID.randomUUID().toString());

    }

    @SuppressWarnings("squid:S1181")
    public static <X extends Throwable> boolean ignoreException(final Class<X> ignore, final Runnable block) {

        try {

            notNull(block).run();

            return true;

        } catch (final Throwable t) {

            if (!ignore.isAssignableFrom(t.getClass())) throw UncheckedException.wrap(t);

            return false;

        }

    }

}
