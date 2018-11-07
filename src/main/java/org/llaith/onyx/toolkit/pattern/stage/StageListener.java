/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.stage;

/**
 * Intended to be used to implement things like transaction control. Context is passed in case
 * the one listener is tracking multiple stages.
 */
public interface StageListener {

    void onStart(Context context);

    void onSuccess(Context context);

    // deliberately do not pass exception! Use exceptionHandler elsewhere if
    // needed. This is purly a notification of failure and need to 'rollback'.
    void onFailure(Context context);

}
