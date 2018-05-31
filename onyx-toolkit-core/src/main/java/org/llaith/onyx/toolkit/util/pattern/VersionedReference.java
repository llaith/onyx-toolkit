/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.pattern;

/**
 *
 */
public class VersionedReference<T> {

    private final Versioned<T> versioned;

    private long lastVersion = -1;

    public VersionedReference(final Versioned<T> versioned) {
        this.versioned = versioned;

        this.lastVersion = versioned.version();
    }

    public boolean isStale() {
        return this.versioned.version() != this.lastVersion;
    }

    public boolean accept() {
        if (this.versioned.version() == this.lastVersion) return false;

        this.lastVersion = this.versioned.version();

        return true;
    }

    public T target() {
        return this.versioned.target();
    }

}
