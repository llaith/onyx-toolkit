/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern.impl;

import org.llaith.onyx.toolkit.util.pattern.ResourceLoan;

/**
 * A very simple partial implementation for resources which merely pass
 * on a pre-set target rather than retrieving one itself.
 */
public abstract class AbstractResourceLoan<T> implements ResourceLoan<T> {

    private final T target;

    protected AbstractResourceLoan(T target) {
        this.target = target;
    }

    @Override
    public T target() {
        return this.target;
    }

}
