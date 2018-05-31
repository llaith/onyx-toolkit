package org.llaith.onyx.toolkit.util.exception;

/**
 *
 */
public interface ThrowableFactory {

    interface ExceptionWithoutCause<T extends Throwable> {

        T newWithMessage(final String msg);

    }

    interface ExceptionWithCause<T extends Throwable> {

        T newWithCause(final String msg, Throwable throwable);

    }

}
