/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.stage;

import java.util.Arrays;
import java.util.function.Consumer;

/**
 * A better version of this would be based around:
 * http://metrics.codahale.com
 */
public class Stage {
    
    private final ContextStack stack;

    private final Context context;

    public Stage(
            final String breadcrumb,
            final StatusReporterFactory statusReporterFactory,
            final StageListener... listeners) {

        this(breadcrumb, new ContextStack(statusReporterFactory, Arrays.asList(listeners)));
    }

    private Stage(final String breadcrumb, final ContextStack stack) {

        // create a new context
        this.context = new Context(stack, breadcrumb);

        // grab stack
        this.stack = stack;

        // push a new context onto the stack
        this.stack.push(this.context);

    }

    public Context stage() {

        return this.context;

    }

    public void execute(final Consumer<Context> callback) {

        callback.accept(this.context);

    }

    public boolean completeStage(final boolean success) {

        // check if we are the current top-of-stack
        if (this.context != this.stack.peek()) throw new IllegalStateException(
                "A nested control must be created by the top of the current control stack");

        // adjust the stack
        this.stack.popTo(this.context, success);

        // return the success value
        return success;

    }

    public Stage startNestedStage(final String breadcrumb) {

        // check if we are the current top-of-stack
        if (this.context != this.stack.peek()) throw new IllegalStateException(
                "A nested control must be created by the top of the current control stack");

        // create and return the new context control containing a context
        return new Stage(breadcrumb, this.stack);

    }

    public boolean completeNestedStage(final Stage control, final boolean success) {

        // system is designed to be closed by a parent to check exception throws are correct
        if (control == this) throw new IllegalStateException(
                "A nested control cannot close itself (you need a parent of passed-in control)");

        // adjust the stack
        this.stack.popTo(control.context, success);

        // return the success value
        return success;

    }

}
