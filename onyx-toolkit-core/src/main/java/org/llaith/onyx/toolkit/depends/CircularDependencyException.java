/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.depends;


import java.util.Collection;
import java.util.HashSet;

public class CircularDependencyException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Collection<Dependency<?>> circular = new HashSet<>();

    public CircularDependencyException(final Collection<Dependency<?>> circular) {
        super("Circular-dependency found");
        this.circular.addAll(circular);
    }

    public Collection<Dependency<?>> getCircular() {
        return this.circular;
    }

}
