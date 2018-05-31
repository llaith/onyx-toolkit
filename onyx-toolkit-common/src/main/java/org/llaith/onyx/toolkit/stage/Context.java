package org.llaith.onyx.toolkit.stage;

import com.google.common.base.Joiner;
import com.google.common.collect.ObjectArrays;
import org.llaith.onyx.toolkit.output.status.StatusReporter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
import static org.llaith.onyx.toolkit.util.lang.Stringify.asString;

/**
 *
 */
public class Context {

    private final ContextStack stack;

    private final UUID uuid = UUID.randomUUID();

    private final String breadcrumb;

    private final LocalDateTime timeStarted;

    private final Map<String,Object> cache = new HashMap<>();

    private static final Joiner joiner = Joiner.on(" | ");

    private StageCompletion completion;

    public Context(final ContextStack stack, final String breadcrumb) {

        this.stack = stack;

        this.breadcrumb = breadcrumb;

        this.timeStarted = LocalDateTime.now();

    }

    StageCompletion completeStage(final boolean success) throws IllegalStateException {

        if (this.completion != null)
            throw new IllegalStateException(format(
                    "Stage has already been completed: %s",
                    asString(this.completion)));

        this.completion = new StageCompletion(this, success);

        return this.completion;

    }

    public StatusReporter reporter() {

        return this.stack.reporterFactory().apply(this);

    }

    public StatusReporter.Emitter reportAt(final StatusReporter.Threshold threshold) {

        return this.stack.reporterFactory().apply(this).outputAt(threshold);

    }

    public void reportIfAt(final StatusReporter.Threshold threshold, final Consumer<StatusReporter.Emitter> consumer) {

        this.stack.reporterFactory().apply(this).outputIfAt(threshold, consumer);

    }

    public StageProgression progression() {

        return this.stack.progression();

    }

    public UUID uuid() {

        return uuid;

    }

    public String breadcrumb() {

        return this.breadcrumb;

    }

    public LocalDateTime timeStarted() {

        return this.timeStarted;

    }

    public long timeElapsed(final ChronoUnit timeUnit) {

        return timeUnit.between(this.timeStarted, LocalDateTime.now());

    }

    public <X> X toCache(final String key, final X x) {

        return this.stack.toCache(key, x);

    }

    public <X> X fromCache(final String key, final Class<X> klass) {

        return this.stack.fromCache(key, klass);

    }

    public String[] breadcrumbArray() {

        return StreamSupport
                .stream(this.stack.spliterator(), false)
                .map(Context::breadcrumb)
                .collect(Collectors.toList())
                .toArray(new String[0]);

    }

    public String breadcrumbs() {

        return joiner.join(this.breadcrumbArray());

    }

    public String breadcrumbs(final String breadcrumb) {

        return joiner.join(ObjectArrays.concat(this.breadcrumbArray(), breadcrumb));

    }

    public boolean isCompleted() {

        return this.completion != null;

    }

    public double completed() {

        // return the decimal seconds form
        return ((double)this.completion.timeCompleted(ChronoUnit.MILLIS) / 1000);

    }

    public String completedString() {

        return (this.completion != null) ?
                format("(@ %s seconds)", this.completed()) :
                "";

    }

    Map<String,Object> cache() {

        return this.cache;

    }

}
