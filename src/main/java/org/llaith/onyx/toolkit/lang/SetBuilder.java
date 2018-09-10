/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * All functions are destructive. If you want a non-destructive one, use copy instead of wrap.
 */
public class SetBuilder<T> {
    
    private final Set<T> delegate;

    public static <X> SetBuilder<X> empty() {
        return wrap(new HashSet<>());
    }

    public static <X> SetBuilder<X> copy(final Collection<X> other) {
        return wrap(new HashSet<>(other));
    }

    public static <X> SetBuilder<X> wrap(final Set<X> other) {
        return new SetBuilder<>(other);
    }

    private SetBuilder(final Set<T> other) {
        this.delegate = other;
    }

    public SetBuilder<T> addAll(final Collection<T> other) {
        
        this.delegate.addAll(other);
     
        return this;
        
    }

    public SetBuilder<T> retainAll(final Collection<T> other) {

        this.delegate.retainAll(other);

        return this;

    }

    public SetBuilder<T> removeAll(final Collection<T> other) {

        this.delegate.removeAll(other);

        return this;

    }

    public boolean isEmpty() {

        return this.delegate.isEmpty();

    }

    public boolean isNotEmpty() {

        return !this.delegate.isEmpty();

    }
    
    public Set<T> asSet() {
        
        return this.delegate;
        
    }

    public Set<T> toSet() {
        
        return new HashSet<>(this.delegate);
        
    }

    public Stream<T> asStream() {

        return this.delegate.stream();

    }

}
