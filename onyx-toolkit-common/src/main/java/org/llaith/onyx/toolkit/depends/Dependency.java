/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.depends;


import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.ArrayList;
import java.util.List;


public class Dependency<T> {

    private final T target;

    private final List<T> dependencies = new ArrayList<>();

    public Dependency(final T target) {
        this.target = Guard.notNull(target);
    }

    public Dependency(final T target, final T[] dependencies) {
        this(target, Guard.toListOrEmpty(dependencies));
    }

    public Dependency(final T target, final List<T> dependencies) {
        this.target = Guard.notNull(target);

        this.dependencies.addAll(Guard.notNull(dependencies));
    }

    public T target() {
        return this.target;
    }

    public List<T> dependencies() {
        return this.dependencies;
    }

    @Override
    public String toString() {
        return this.target+" <- "+this.dependencies;
    }

}
