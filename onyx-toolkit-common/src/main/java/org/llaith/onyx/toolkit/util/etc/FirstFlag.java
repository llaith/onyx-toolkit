/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.etc;

/**
 * Just a little wrapper to make it prettier. It's going to be slower than a
 * direct approach, so bear that in mind. Note either check will toggle the
 * flag off.
 */
public class FirstFlag {

    private boolean flag = true;

    public boolean first() {
        if (this.flag) {
            this.flag = false;
            return true;
        }
        return false;
    }

    public boolean notFirst() {
        return !this.first();
    }

}
