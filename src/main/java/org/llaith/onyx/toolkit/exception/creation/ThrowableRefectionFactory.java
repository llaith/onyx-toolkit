package org.llaith.onyx.toolkit.exception.creation;

import org.llaith.onyx.toolkit.exception.creation.ThrowableFactory.ExceptionWithCause;
import org.llaith.onyx.toolkit.exception.creation.ThrowableFactory.ExceptionWithoutCause;

import static org.llaith.onyx.toolkit.exception.creation.ThrowableReflectionUtil.throwableFor;

/**
 *
 */
public class ThrowableRefectionFactory<T extends Throwable> implements ExceptionWithCause<T>, ExceptionWithoutCause<T> {

    private final Class<T> klass;

    public ThrowableRefectionFactory(final Class<T> klass) {
        this.klass = klass;
    }

    @Override
    public T newWithMessage(final String msg) {
        return throwableFor(this.klass, msg);
    }

    @Override
    public T newWithCause(final String msg, final Throwable throwable) {
        return throwableFor(this.klass, msg, throwable);
    }

}
