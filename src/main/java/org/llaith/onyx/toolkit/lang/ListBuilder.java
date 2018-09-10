/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 *
 */
public class ListBuilder<T> {
    
    private final List<T> delegate;

    public static <X> ListBuilder<X> empty() {
        return wrap(new ArrayList<>());
    }

    public static <X> ListBuilder<X> copy(final Collection<X> other) {
        return wrap(new ArrayList<>(other));
    }

    public static <X> ListBuilder<X> wrap(final List<X> other) {
        return new ListBuilder<>(other);
    }

    private ListBuilder(final List<T> other) {
        this.delegate = other;
    }

    public ListBuilder<T> add(final T item) {

        this.delegate.add(item);

        return this;

    }

    public ListBuilder<T> addAll(final Collection<T> other) {

        this.delegate.addAll(other);

        return this;

    }

    public List<T> asList() {
        
        return this.delegate;
        
    }

    public List<T> toList() {
        
        return new ArrayList<>(this.delegate);
        
    }

    public Stream<T> asStream() {

        return this.delegate.stream();

    }

}
