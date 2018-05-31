/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.results;

import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.exception.ThrowableFactory.ExceptionWithoutCause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Changed mind, it must always go into a list. Wrap it in a list if need be.
 */
public class ResultSet<T> {

    private final Set<T> results;

    private final ExceptionWithoutCause<RuntimeException> exceptionFactory;

    public ResultSet(final Set<T> results) {

        this(results, IllegalArgumentException::new);

    }

    @SuppressWarnings("unchecked")
    public ResultSet(final Set<T> results, final ExceptionWithoutCause<RuntimeException> exceptionFactory) {

        this.results = results != null ?
                results :
                Collections.emptySet();

        this.exceptionFactory = Guard.notNull(exceptionFactory);

    }

    public ResultSet<T> expectAtLeastOneResult(final String msg) {

        if (results.isEmpty()) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at least one result but there were none."));

        return this;

    }

    public ResultSet<T> expectAtMostOneResult(final String msg) {

        if (results.size() > 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at most a one result but there were " + results.size() + "."));

        return this;

    }

    public ResultSet<T> expectExactlyOneResult(final String msg) {

        if (results.size() != 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage(msg + ": expected exactly one result but there were " + results.size() + "."));

        return this;

    }

    public ResultSet<T> staleIfEmpty(final String msg) {

        if (results.isEmpty()) throw new StaleResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected to have results but there were none."));

        return this;

    }

    public void forEach(final Consumer<T> consumer) {

        this.results.forEach(consumer);

    }

    /**
     * Always give a new list, safe to mutate
     */
    public Set<T> toSet() {
        return new HashSet<>(this.results);
    }

    public Set<T> asSet() {
        return Collections.unmodifiableSet(this.results);
    }

    public <K> Map<K,T> indexValues(final Function<T,K> keyTransform) {

        return this.results
                .stream()
                .collect(Collectors.toMap(
                        keyTransform,
                        Function.identity()));

    }

    /**
     * May lose elements moving from list to set.
     */
    public ResultList<T> toResultList() {

        return new ResultList<>(
                new ArrayList<>(this.results),
                this.exceptionFactory);

    }

    public <R> ResultSet<R> mapResults(final Function<T,R> mapper) {

        if (this.results == null) return null;

        return new ResultSet<>(
                this.results.stream()
                            .map(mapper::apply)
                            .collect(Collectors.toSet()));

    }

}
