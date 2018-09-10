/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.depends;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: nos
 * Date: 19/03/13
 */
public class UnsatisfiedDependencyException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Collection<Dependency<?>> unsatisfied;

    public UnsatisfiedDependencyException(final Collection<Dependency<?>> unsatisfied) {
        this.unsatisfied = unsatisfied;
    }

    public Collection<Dependency<?>> getUnsatisfied() {
        return unsatisfied;
    }
}
