/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.activity;

import org.llaith.onyx.toolkit.exception.ExceptionSuppressor;
import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 *
 */
public class ActivityTracker implements Activity {

    private static final Logger logger = LoggerFactory.getLogger(ActivityTracker.class);

    private final Stack<Activity> deactivated = new Stack<>();
    private final Stack<Activity> activated = new Stack<>();

    public <X> X track(final X x) {
        if (x instanceof Activity) {
            if (((Activity)x).isActive()) throw new IllegalArgumentException("Instance is already activated.");
            this.deactivated.push((Activity)x);
        }
        return x;
    }

    public <X, Y extends Iterable<X>> Y trackAll(final Y all) {
        for (X x : all) {
            this.track(x);
        }
        return all;
    }

    @Override
    public boolean isActive() {
        return !this.activated.isEmpty();
    }

    @Override
    public void activate() {
        if (this.isActive()) throw new UncheckedException("Instance is already activated.");
        // fail on first exception
        try {
            while (!this.deactivated.isEmpty()) {
                final Activity act = this.deactivated.pop();
                act.activate(); // contract(acquire->record): if it doesn't complete then it doesn't need close.
                this.activated.push(act);
            }
        } catch (Exception e) {
            throw UncheckedException.wrap(e);
        }
    }

    @Override
    public void deactivate() {
        if (!this.isActive()) throw new UncheckedException("Instance is already deactivated.");

        // fail on last exception
        ExceptionSuppressor suppressor = new ExceptionSuppressor();

        while (!this.activated.isEmpty()) {
            try {
                final Activity act = this.activated.pop();
                act.deactivate();
                this.deactivated.push(act); // contract(record->release): we only try once to close.
            } catch (Exception e) {
                suppressor.suppress(e);
            }
        }

        suppressor.propagate();

    }

}
