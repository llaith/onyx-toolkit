/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fn;

import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.llaith.onyx.toolkit.exception.ExceptionUtil.wrapException;
import static org.llaith.onyx.toolkit.lang.Guard.notNull;

@SuppressWarnings("squid:S1200")
public class ExcecutionUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcecutionUtil.class);

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

    public static void rethrowException(final Block<Exception> block) {

        ExcecutionUtil.rethrow(block);

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
