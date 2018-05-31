package org.llaith.onyx.toolkit.repository.impl;

import org.llaith.onyx.toolkit.repository.RepositoryView;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Stream;

/**
 *
 */
public class DefaultRepositoryView<T> extends DefaultRepository<T> implements RepositoryView<T> {

    public DefaultRepositoryView(final Set<T> values) {

        super(values);

    }

    @Override
    public Set<T> set() {

        return Collections.unmodifiableSet(this.values);

    }

    @Override
    public Stream<T> stream() {

        return this.values.stream();

    }

    @Override
    public void forEach(final Consumer<? super T> action) {

        stream().forEach(action);

    }

    @Override
    public <R, A> R collect(final Collector<? super T,A,R> collector) {

        return stream().collect(collector);

    }

    @Override
    public Iterator<T> iterator() {

        return this.values.iterator();

    }

}
