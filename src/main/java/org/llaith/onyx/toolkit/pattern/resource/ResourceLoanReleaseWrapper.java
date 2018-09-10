/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.resource;

/**
 *
 */
public class ResourceLoanReleaseWrapper<T> implements ResourceLoan<T> {

    public static <T> ResourceLoan<T> wrap(final ResourceLoan<T> delegate) {
        return new ResourceLoanReleaseWrapper<>(delegate);
    }

    private final ResourceLoan<T> delegate;

    private boolean released = false;

    public ResourceLoanReleaseWrapper(final ResourceLoan<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public T target() {
        if (released) return null;
        return this.delegate.target();
    }

    @Override
    public void releaseTarget() {
        this.delegate.releaseTarget();
        this.released = true;
    }

}
