/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

/**
 * Note: Could not think of a better way to do this, so forced to do a nasty
 * hack to deal with cycles without passing any objects down the interface
 * (yuck). This means it's a tad fragile. I moved it into it's own wrapper
 * class in case I need to drop it in a hurry. Obviously not threadsafe in any
 * way.
 */
public class ChangeTrackedCyclicWrapper implements ChangeTracked {

    private final ChangeTracked delegate;

    private transient boolean checkDirty = false;
    private transient boolean checkStale = false;
    private transient boolean checkConflicted = false;
    private transient boolean skipDirty = false;
    private transient boolean skipStale = false;
    private transient boolean skipConflicted = false;
    private transient boolean skipAccept = false;
    private transient boolean skipCancel = false;

    public ChangeTrackedCyclicWrapper(final ChangeTracked delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isDirty() {
        // short circuit
        if (this.skipDirty) {
            this.skipDirty = false;
            return this.checkDirty;
        }

        // eval
        this.skipDirty = true;
        try {
            this.checkDirty = this.delegate.isDirty();
            return this.checkDirty;
        } finally {
            this.skipDirty = false;
        }
    }

    @Override
    public boolean isStale() {
        // short circuit
        if (this.skipStale) {
            this.skipStale = false;
            return this.checkStale;
        }

        // eval
        this.skipStale = true;
        try {
            this.checkStale = this.delegate.isStale();
            return this.checkStale;
        } finally {
            this.skipStale = false;
        }
    }

    @Override
    public boolean isConflicted() {
        // short circuit
        if (this.skipConflicted) {
            this.skipConflicted = false;
            return this.checkConflicted;
        }

        // eval
        this.skipConflicted = true;
        try {
            this.checkConflicted = this.delegate.isConflicted();
            return this.checkConflicted;
        } finally {
            this.skipConflicted = false;
        }
    }

    @Override
    public void acceptChanges() {
        // short circuit
        if (this.skipAccept) {
            this.skipAccept = false;
            return;
        }

        // eval
        this.skipAccept = true;
        try {
            this.delegate.acceptChanges();
        } finally {
            this.skipAccept = false;
        }

    }

    @Override
    public void cancelChanges() {
        // short circuit
        if (this.skipCancel) {
            this.skipCancel = false;
            return;
        }

        // eval
        this.skipCancel = true;
        try {
            this.delegate.cancelChanges();
        } finally {
            this.skipCancel = false;
        }

    }

}
