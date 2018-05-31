/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.results;

import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.exception.ThrowableFactory.ExceptionWithoutCause;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Changed mind, it must always go into a list. Wrap it in a list if need be.
 */
public class ResultList<T> {


    private final List<T> results;

    private final ExceptionWithoutCause<RuntimeException> exceptionFactory;

    public ResultList(final List<T> results) {

        this(results, IllegalStateException::new);

    }

    @SuppressWarnings("unchecked")
    public ResultList(final List<T> results, final ExceptionWithoutCause<RuntimeException> exceptionFactory) {

        this.results = results != null ?
                results :
                Collections.emptyList();

        this.exceptionFactory = Guard.notNull(exceptionFactory);

    }

    public ResultList<T> expectAtLeastOneResult(final String msg) {

        if (results.isEmpty()) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at least one result but there were none."));

        return this;

    }

    public ResultList<T> expectAtMostOneResult(final String msg) {

        if (results.size() > 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected at most a one result but there were " + results.size() + "."));

        return this;

    }

    public ResultList<T> expectExactlyOneResult(final String msg) {

        if (results.size() != 1) throw new UnexepctedResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected exactly one result but there were " + results.size() + "."));

        return this;

    }

    public ResultList<T> staleIfEmpty(final String msg) {

        if (results.isEmpty()) throw new StaleResultException(
                msg,
                this.exceptionFactory.newWithMessage("Expected to have results but there were none."));

        return this;

    }

    public ResultList<T> filter(final Predicate<T> predicate) {

        return new ResultList<>(this.results
                                        .stream()
                                        .filter(predicate)
                                        .collect(Collectors.toList()));

    }

    public void forEach(final Consumer<T> consumer) {

        this.results.forEach(consumer);

    }

    public Optional<List<T>> toOptional() {
        return Optional.of(this.results);
    }

    public Stream stream() {

        return this.results.stream();

    }

    /**
     * Always give a new list, safe to mutate
     */
    public List<T> toList() {
        return new ArrayList<>(this.results);
    }

    public List<T> asList() {
        return Collections.unmodifiableList(this.results);
    }

    public boolean isEmpty() {
        return this.results.isEmpty();
    }

    public boolean notEmpty() {
        return !this.results.isEmpty();
    }

    /**
     * Always give a new list, safe to mutate. We don't have a corresponding asSortedList because
     * it may or may not change the order of the real list (indeterminate).
     */
    public List<T> toSortedList(Comparator<? super T> comparator) {

        final List<T> list = toList();

        Collections.sort(list, comparator);

        return list;

    }

    /**
     * May lose elements moving from list to set.
     */
    public ResultSet<T> toResultSet() {

        return new ResultSet<>(
                new HashSet<>(this.results),
                this.exceptionFactory);

    }

    /**
     *
     */
    public ResultObject<T> firstResult() {

        return new ResultObject<>(
                this.results.isEmpty() ? null : this.results.get(0),
                this.exceptionFactory); // of course, may be null

    }

    public <R> ResultList<R> mapResults(final Function<T,R> mapper) {

        if (this.results == null) return null;

        return new ResultList<>(
                this.results.stream()
                            .map(mapper)
                            .collect(Collectors.toList()));

    }

}
