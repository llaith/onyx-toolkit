/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern.impl;

import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.pattern.ResourceLoan;

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
