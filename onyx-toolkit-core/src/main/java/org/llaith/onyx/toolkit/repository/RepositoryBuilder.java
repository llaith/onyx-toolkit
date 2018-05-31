/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.repository;

import java.util.stream.Stream;

/**
 * Guava's FluentIterable now has almost all the functionality that this used
 * to have. The part that remains is the usage patterns of: (a) passing around
 * a heterogeneous collection and turning it typed only when iterating,
 * (b) snapshotting the collection while iterating and being able to add and
 * remove elements from the original collection while iterating, and (c)
 * supporting chained parent-child repositories.
 */
public interface RepositoryBuilder<T> extends Repository<T> {

    RepositoryBuilder<T> add(T item);

    RepositoryBuilder<T> addAll(Iterable<T> items);

    RepositoryBuilder<T> addAll(Stream<T> stream);

    RepositoryBuilder<T> remove(T item);

    RepositoryBuilder<T> removeAll(Iterable<T> items);

}
