/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.llaith.onyx.toolkit.util.lang.StringUtil;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.llaith.onyx.toolkit.util.lang.Guard.notNull;

@SuppressWarnings("squid:S1200")
public class ExceptionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

    @FunctionalInterface
    public interface Block<E extends Exception> {

        void execute() throws E;

    }

    @FunctionalInterface
    public interface ReturnBlock<R, E extends Exception> {

        R execute() throws E;

    }

    @FunctionalInterface
    public interface FutureBlock<T, E extends Exception> {

        Future<T> execute() throws E;

    }

    @FunctionalInterface
    public interface SupplierBlock<S, E extends Exception> {

        S execute() throws E;

    }

    @FunctionalInterface
    public interface ConsumerBlock<T, E extends Exception> {

        void execute(T t) throws E;

    }

    @FunctionalInterface
    public interface FunctionBlock<T, R, E extends Exception> {

        R execute(T t) throws E;

    }

    public static RuntimeException wrapException(final Throwable e) {
        if (e instanceof Error) throw (Error)e;
        if (e instanceof RuntimeException) return (RuntimeException)e;
        return new RuntimeException(e);
    }

    public static void rethrow(final Block<Exception> block) {

        try {

            block.execute();

        } catch (Exception e) {

            throw wrapException(e);
        }

    }

    public static <X> X rethrowOrReturn(final ReturnBlock<X,Exception> block) {

        try {

            return block.execute();

        } catch (Exception e) {

            throw wrapException(e);
        }

    }

    public static <X> X call(
            final ReturnBlock<X,Exception> block,
            final Consumer<X> successHandler,
            final Consumer<Exception> errorHandler) {

        try {

            final X ret = block.execute();

            successHandler.accept(ret);

            return ret;

        } catch (Exception e) {

            errorHandler.accept(e);

            throw UncheckedException.wrap(e);

        }

    }


    public static <X> Future<X> rethrowOrExecuteFuture(final FutureBlock<X,Exception> block) {

        try {

            return block.execute();

        } catch (Exception e) {

            throw wrapException(e);
        }

    }

    public static <X> Supplier<X> rethrowOrSupply(final SupplierBlock<X,Exception> supplier) {

        return () -> {

            try {

                return supplier.execute();

            } catch (Exception e) {

                throw wrapException(e);
            }

        };

    }

    public static <X> Consumer<X> rethrowOrConsume(final ConsumerBlock<X,Exception> consumer) {

        return (X x) -> {

            try {

                consumer.execute(x);

            } catch (Exception e) {

                throw wrapException(e);
            }

        };

    }

    public static <X, Y> Function<X,Y> rethrowOrMap(final FunctionBlock<X,Y,Exception> function) {

        return (X x) -> {

            try {

                return function.execute(x);

            } catch (Exception e) {

                throw wrapException(e);
            }

        };

    }

    @SuppressWarnings("squid:S2142")
    public static void uninteruptableBlock(final Block<InterruptedException> block) {

        while (true) {

            try {

                block.execute();

                return;

            } catch (InterruptedException e) {

                logger.debug("Ignoring cancel request, need to flush cache.", e);

            }

        }

    }

    @SuppressWarnings("squid:S2142")
    public static <S> S uninteruptableSupplier(final SupplierBlock<S,InterruptedException> block) {

        while (true) {

            try {

                return block.execute();

            } catch (InterruptedException e) {

                logger.debug("Ignoring cancel request, need to flush cache.", e);

            }

        }

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

    public static void rethrowException(final Block<Exception> block) {

        ExceptionUtil.rethrow(block);

    }

    public static Exception returnException(final Block<Exception> block) {

        try {

            notNull(block).execute();

            return null;

        } catch (Exception e) {

            return e;

        }

    }

    @SuppressWarnings("ThrowableNotThrown")
    public static void suppressException(final Block<Exception> block) {

        suppressException(block, e -> logger.debug("Suppressing exception:", e));

    }

    public static void suppressException(final Block<Exception> block, final Consumer<Exception> exceptionConsumer) {

        try {

            notNull(block).execute();

        } catch (Exception e) {

            notNull(exceptionConsumer).accept(e);

        }

    }

    public static <X> X suppressExceptionOrReturn(final ReturnBlock<X,Exception> block, final Function<Exception,X> exceptionFn) {

        try {

            return notNull(block).execute();

        } catch (Exception e) {

            return notNull(exceptionFn).apply(e);

        }

    }

    public static void suppressExceptions(final Collection<Block<Exception>> blocks) {

        for (final Block<Exception> block : blocks) suppressException(block);

    }

    @SafeVarargs
    public static void suppressExceptions(final Block<Exception>... blocks) {

        suppressExceptions(Arrays.asList(blocks));

    }

    public static <X> X suppressExceptionAndReturn(final ReturnBlock<X,Exception> block, final Consumer<Exception> exceptionConsumer) {

        try {

            return block.execute();

        } catch (Exception e) {

            notNull(exceptionConsumer).accept(e);

            return null;

        }

    }

}
