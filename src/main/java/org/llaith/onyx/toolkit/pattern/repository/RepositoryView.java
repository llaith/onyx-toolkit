package org.llaith.onyx.toolkit.pattern.repository;

import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 *
 */
public interface RepositoryView<T> extends Repository<T>, Iterable<T> {

    Set<T> set();

    Stream<T> stream();

    // one of the most common stream functions
    void forEach(Consumer<? super T> action);

    // one of the most common stream functions
    <R, A> R collect(Collector<? super T,A,R> collector);

}
