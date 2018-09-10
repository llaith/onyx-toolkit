/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.holder;

/**
 *
 */
public class OkFlag {

    private boolean ok = true;

    public boolean eval(final boolean in) {
        if (!in) this.ok = false;
        return in;
    }

    public boolean isOk() {
        return this.ok;
    }

}
