/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.exception;

import com.google.common.base.Throwables;

/**
 *
 */
public class ExceptionSuppressor {

    private Exception firstEx;

    public void suppress(final Exception t) {

        if (this.firstEx == null) this.firstEx = t;

        else this.firstEx.addSuppressed(t);

    }

    public boolean hasSuppressed() {

        return this.firstEx != null;

    }

    public String asString() {

        if (this.firstEx == null) return null;

        return Throwables.getStackTraceAsString(this.firstEx);

    }

    public void propagate() {

        if (!this.hasSuppressed()) return;

        throw UncheckedException.wrap(this.firstEx);

    }

    public Exception exception() {

        return this.firstEx;

    }

}
