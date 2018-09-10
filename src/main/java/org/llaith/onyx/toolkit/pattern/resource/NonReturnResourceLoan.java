/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.resource;

import org.llaith.onyx.toolkit.lang.Guard;

/**
 *
 */
public class NonReturnResourceLoan<T> implements ResourceLoan<T> {

    private T target;

    public NonReturnResourceLoan(T target) {
        this.target = Guard.notNull(target);
    }

    @Override
    public T target() {
        return this.target;
    }

    @Override
    public void releaseTarget() {
        this.target = null;
    }

}
