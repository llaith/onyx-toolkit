/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.holder;

/**
 *
 */
public class Counter {

    private final int start;

    private int counter;

    public Counter() {
        this(0);
    }

    public Counter(final int start) {
        this.start = start;
        this.counter = start;
    }

    public int nextValue() {
        return this.counter++;
    }

    public int currentValue() {
        return this.counter;
    }

    public boolean isFirst() {
        return this.start == this.counter;
    }

    public boolean notFirst() {
        // not delegate to isFirst because already quite a performance impact from normal anyway.
        return this.start != this.counter;
    }

}
