/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

/**
 * Activity should be used for long running 'services' that may need to be
 * paused/resumed to preserve resources.
 */
public interface Activity {

    boolean isActive();

    void activate();

    void deactivate();

}
