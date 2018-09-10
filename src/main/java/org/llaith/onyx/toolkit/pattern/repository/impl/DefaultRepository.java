package org.llaith.onyx.toolkit.pattern.repository.impl;

import com.google.common.collect.FluentIterable;
import org.llaith.onyx.toolkit.pattern.repository.Repository;
import org.llaith.onyx.toolkit.pattern.repository.RepositoryView;

import java.util.HashSet;
import java.util.Set;

/**
 *
 */
public abstract class DefaultRepository<T> implements Repository<T> {

    protected final Set<T> values = new HashSet<>();

    protected DefaultRepository(final Set<T> values) {

        this.values.addAll(values);

    }

    @Override
    public <X extends T> RepositoryView<X> select(final Class<X> klass) {

        return new DefaultRepositoryView<>(
                FluentIterable
                        .from(this.values)
                        .filter(klass)
                        .toSet());

    }

}
