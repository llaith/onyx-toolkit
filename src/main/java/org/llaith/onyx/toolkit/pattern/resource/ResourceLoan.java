/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.resource;

/**
 * When calling releaseTarget(), a subsequent call to target() must return null! This is the safest way to avoid
 * re-using released resources. A nullcheck on the target() is simple enough to avoid needing a isReleased() method,
 * especially as the whole concept of the ResourceLoan means you shouldn't need to check. It's yours until you are
 * done. If you want another, take the resourceManager as a param instead.
 */
public interface ResourceLoan<T> {

    T target();

    void releaseTarget();

}
