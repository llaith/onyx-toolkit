/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.fault;

import org.llaith.onyx.toolkit.output.memo.Memo;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.Arrays;
import java.util.Date;

/**
 * Instead of having warnings which are treated as errors if not supressed, we have failures which will be errors if
 * they are not suppressed. And instead of the distinction being baked into the fault itself, we decide when we raise
 * it - even if the error dtoField has a suppression, the 'raiser' may choose not to use it. This is because it may be
 * context sensitive, such as by logged in user or when some other condition has occurred. There is no reason to not
 * allow multiple suppression tokens for the one fault if they are used in different contexts, eg - first pass through
 * a system and a subsequent pass, for example. The net effect of this is that it's a very easy to use and lazy system.
 * You can just continue to raise failures instead of throw exceptions, and only if you care about it being ignorable do
 * you need to create a token, and even then you don't need to always use it.
 */
public class Fault {

    private final Date occurrence = new Date();
    private final FaultLocation location;
    private final String message;
    private final SuppressionToken suppressionToken;
    private Exception exception;
    private Memo description;
    private boolean suppressed;

    private StackTraceElement[] stackTrace;

    public Fault(final FaultLocation location, final String message) {
        this(location, message, null, null, null);
    }

    public Fault(final FaultLocation location, final String message, final SuppressionToken suppressionToken) {
        this(location, message, suppressionToken, null, null);
    }

    protected Fault(final FaultLocation location, final String message, final SuppressionToken suppressionToken,
                    final Exception exception, final Memo description) {

        this.location = Guard.notNull(location);
        this.message = Guard.notNull(message);
        this.suppressionToken = suppressionToken;
        this.exception = exception;
        this.description = description;

        this.stackTrace =
                Thread.currentThread()
                      .getStackTrace();

    }

    public Fault withException(final Exception exception) {
        this.exception = Guard.notNull(exception);

        return this;
    }

    public Fault withDescription(final Memo memo) {
        this.description = memo;

        return this;
    }

    public Fault withSuppression(final boolean suppressed) {
        this.suppressed = suppressed;

        return this;
    }

    public Date occurrence() {
        return this.occurrence;
    }

    public FaultLocation location() {
        return location;
    }

    public SuppressionToken suppressionToken() {
        return suppressionToken;
    }

    public String message() {
        return message;
    }

    public Exception exception() {
        return exception;
    }

    public Memo description() {
        return this.description;
    }

    public boolean isSuppressed() {
        return this.suppressed;
    }

    public StackTraceElement[] getStackTrace() {
        return stackTrace;
    }

    public String getStackTraceAsString() {
        return Arrays.toString(stackTrace);
    }

}
