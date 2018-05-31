/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

import java.io.Closeable;

/**
 * Fundamental pattern for any object that needs to be borrowed and returned.
 * This can be anything from a single resource you want to lock before use,
 * or a traditional pool of something that is sessioned like connections, or
 * things you want to check in and out of some sort of repository.
 */
public interface ResourceManager<T> extends Closeable {

    ResourceLoan<T> acquire();

}
