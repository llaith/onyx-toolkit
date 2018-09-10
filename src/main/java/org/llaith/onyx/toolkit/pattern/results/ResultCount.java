package org.llaith.onyx.toolkit.pattern.results;

import org.llaith.onyx.toolkit.exception.creation.ThrowableFactory.ExceptionWithoutCause;
import org.llaith.onyx.toolkit.lang.Guard;

/**
 *
 */
public class ResultCount {

    private final int count;

    private final ExceptionWithoutCause<RuntimeException> exceptionFactory;

    public ResultCount(final int count) {

        this(count, IllegalStateException::new);

    }

    public ResultCount(final int count, final ExceptionWithoutCause<RuntimeException> exceptionFactory) {

        this.count = count;

        this.exceptionFactory = Guard.notNull(exceptionFactory);

    }

    public ResultCount expectAtLeastOneCount(final String msg) {

        if (count < 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at least one count but there were none."));

        return this;

    }

    public ResultCount expectAtMostOneCount(final String msg) {

        if (count > 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at most a one result but there were " + count + "."));

        return this;

    }

    public ResultCount expectExactlyOneCount(final String msg) {

        if (count != 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected one result but there were " + count + "."));

        return this;

    }

    public ResultCount staleIfNoCount(final String msg) {

        if (count < 1) throw new StaleResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected to have results but there were none."));

        return this;

    }


    public boolean isNone() {

        return this.count < 1;

    }

    public int count() {

        return this.count;

    }

}
