/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import org.apache.commons.lang3.text.StrLookup;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.slf4j.Logger;
import org.llaith.onyx.toolkit.util.exception.ExceptionHandler;

import static java.lang.String.format;
import static org.slf4j.LoggerFactory.getLogger;

/**
 *
 */
public class StringLookup extends StrLookup<Object> {

    private static final Logger Log = getLogger(StringLookup.class);

    public static StrSubstitutor substitute(final Lookup lookup) {

        return new StrSubstitutor(new StringLookup(lookup));

    }

    public static StrSubstitutor substitute(final Lookup lookup, final ExceptionHandler<RuntimeException> handler) {

        return new StrSubstitutor(new StringLookup(lookup, handler));

    }

    public interface Lookup {

        String lookup(String key);

    }

    private final Lookup lookup;

    private final ExceptionHandler<RuntimeException> handler;

    public StringLookup(final Lookup lookup) {

        this(lookup, e -> {
            throw e;
        });

    }

    public StringLookup(final Lookup lookup, final ExceptionHandler<RuntimeException> handler) {

        this.lookup = Guard.notNull(lookup);

        this.handler = Guard.notNull(handler);

    }

    @Override
    public String lookup(String key) {

        try {

            return this.lookup.lookup(key);

        } catch (RuntimeException e) {

            Log.error(format("Could not substitute the expression '${%s}'", key));

            this.handler.exceptionCaught(e);

        }

        return null;

    }

}
