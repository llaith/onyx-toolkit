/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.repository.impl;

import org.llaith.onyx.toolkit.pattern.repository.RepositoryBuilder;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.stream.Stream;

/**
 */
public class DefaultRepositoryBuilder<T> extends DefaultRepository<T> implements RepositoryBuilder<T> {

    public DefaultRepositoryBuilder() {

        super(Collections.emptySet());

    }

    @Override
    public RepositoryBuilder<T> add(final @Nullable T item) {

        if (item != null) this.values.add(item);

        return this;

    }


    @Override
    public RepositoryBuilder<T> addAll(final @Nullable Iterable<T> items) {

        if (items != null) items.forEach(this.values::add);

        return this;

    }

    @Override
    public RepositoryBuilder<T> addAll(final @Nullable Stream<T> stream) {

        if (stream != null) stream.forEach(this.values::add);

        return this;

    }

    @Override
    public RepositoryBuilder<T> remove(final @Nullable T item) {

        if (item != null) this.values.remove(item);

        return this;

    }

    @Override
    public RepositoryBuilder<T> removeAll(final @Nullable Iterable<T> items) {

        if (items != null) items.forEach(this.values::remove);

        return this;

    }

}
