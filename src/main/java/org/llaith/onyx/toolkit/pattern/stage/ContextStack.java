/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.stage;

import org.llaith.onyx.toolkit.pattern.display.status.StatusReporter;
import org.llaith.onyx.toolkit.lang.Guard;
import org.llaith.onyx.toolkit.reflection.TypeUtil;

import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

/**
 *
 */
public class ContextStack implements Iterable<Context> {

    private final CompoundStageListener listeners;

    private final Stack<Context> stack = new Stack<>();

    private final StageProgression progression = new StageProgression();

    private final StatusReporterFactory statusReporterFactory;

    public ContextStack(
            final StatusReporterFactory statusReporterFactory,
            final List<StageListener> listeners) {

        super();

        this.statusReporterFactory = Guard.notNull(statusReporterFactory);

        this.listeners = new CompoundStageListener(listeners);

    }

    public CompoundStageListener listeners() {
        return listeners;
    }

    public StageProgression progression() {
        return progression;
    }

    public Function<Context,StatusReporter> reporterFactory() {
        return this.statusReporterFactory;
    }

    public Context peek() {

        return this.stack.peek();

    }

    public Context push(final Context context) {

        // push the context on
        this.stack.push(context);

        // fire the listeners (note an exception here will veto the context)
        this.listeners.onStart(context);

        // return context
        return context;

    }

    public void popTo(final Context context, final boolean success) {

        // find the request context
        final int pos = this.stack.search(context);

        // check we even have the token (it might have been popped already) 
        if (pos == -1) throw new IllegalStateException(
                "The requested token does not exist on the stack (the passed-in token has no parent)");

        // don't pop back more than one level
        if (!this.stack.peek().equals(context))
            throw new IllegalStateException("The head of the stack is not the requested token. Missing or duplicate pops() detected.");

        // grab the top of stack
        final Context top = stack.peek();

        // add to progression
        this.progression.addCompletion(top.completeStage(success));

        // fire listeners for completion
        if (success) this.listeners.onSuccess(top);
        else this.listeners.onFailure(top);

        // pop back one level
        this.stack.pop();

    }

    public <X> X toCache(final String key, final X x) {

        // find if the key already exists
        final Object o = this.fromCache(key, x.getClass());

        // if it does, its an error, we don't allow data hiding
        if (o != null) throw new IllegalArgumentException(String.format(
                "A value with key of: '%s' already exists in the context stack with the value: '%s'",
                key,
                o));

        // then stick the value on the top of the stage cache
        this.stack.peek().cache().put(key, x);

        // and return
        return x;

    }

    public <X> X fromCache(final String key, final Class<X> klass) {

        // search the whole stack for the key, it's unique, so it won't matter which direction
        for (final Context context : this.stack) {

            // get the key from the next context
            final Object o = context.cache().get(key);

            // return only if it isn't null
            if (o != null) return TypeUtil.cast(klass, o);

        }

        // ok, we didn't find it
        return null;

    }

    @Override
    @Nonnull
    public Iterator<Context> iterator() {
        return this.stack.iterator();
    }

}
