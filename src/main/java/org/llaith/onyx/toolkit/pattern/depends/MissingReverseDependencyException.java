/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.depends;


public class MissingReverseDependencyException extends Exception {

    private static final long serialVersionUID = 1L;

    private final Object missing;

    public MissingReverseDependencyException(final Object missing) {
        super("Missing dependency information.");
        this.missing = missing;
    }

    public Object getMissing() {
        return missing;
    }
}
