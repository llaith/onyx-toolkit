/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.results;

import org.llaith.onyx.toolkit.exception.creation.ThrowableFactory.ExceptionWithoutCause;
import org.llaith.onyx.toolkit.lang.Guard;

import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

/**
 *
 */
public class ResultObject<T> {

    private final T result;

    private final ExceptionWithoutCause<RuntimeException> exceptionFactory;

    public ResultObject() {

        this(null, IllegalStateException::new);

    }

    public ResultObject(T result) {

        this(result, IllegalStateException::new);

    }

    public ResultObject(T result, final ExceptionWithoutCause<RuntimeException> exceptionFactory) {

        this.result = result;

        this.exceptionFactory = Guard.notNull(exceptionFactory);

    }

    public ResultObject<T> expectNotNull(final String msg) {

        if (result == null) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at least one result but there were none."));

        return this;

    }

    public ResultObject<T> expectNull(final String msg) {

        if (result != null) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected to be no results but there was at least one."));

        return this;

    }

    public T expectResultElseError(final String msg) {

        return this.expectNotNull(msg).result();

    }

    public ResultObject<T> staleIfNull(final String msg) {

        if (result != null) throw new StaleResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected to be no results but there was at least one."));

        return this;

    }

    public ResultList<T> toListResults() {

        return new ResultList<>(
                this.result == null ?
                        Collections.emptyList() :
                        Collections.singletonList(this.result),
                this.exceptionFactory);

    }

    public <R> ResultObject<R> mapResult(final Function<T,R> mapper) {

        if (this.result == null) return null;

        return new ResultObject<>(mapper.apply(this.result));

    }

    public Optional<T> toOptional() {
        return Optional.of(this.result);
    }

    public T result() {

        return this.result;

    }

}
