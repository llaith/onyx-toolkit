/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.depends;

import org.llaith.onyx.toolkit.lang.Guard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @param <S>
 */
public final class ReverseDependency<S> {

    private final Dependency<S> dependency;

    private final List<S> reverseDependants = new ArrayList<>();

    public ReverseDependency(final Dependency<S> dependency) {
        this.dependency = Guard.notNull(dependency);
    }

    public Dependency<S> dependency() {
        return this.dependency;
    }

    public List<S> reverseDependants() {
        return this.reverseDependants;
    }

}
