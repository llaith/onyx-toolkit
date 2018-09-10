/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.context;


import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a similar class to Register, the very simple way to config and share services. Primarily
 * this avoids having to do lots of Guard.notNull()'s around the code, and also stops duplicate
 * registrations.
 *
 * USAGE: be careful of anon config classes like new SomeConfig() {{}} It will work, but you will only get
 * to put one in.
 */
public class Configs {

    private final Map<Class<?>,Object> index = new HashMap<>();

    public Configs register(final Object config) {

        final Class<?> klass = config.getClass().isAnonymousClass() ?
                config.getClass().getSuperclass() :
                config.getClass();

        if (index.containsKey(klass)) throw new UncheckedException("Duplicate registration of config class: "+klass);

        this.index.put(
                klass,
                config);

        return this;

    }

    public boolean hasConfigFor(final Class<?> klass) {

        return this.index.containsKey(klass);

    }

    @SuppressWarnings("unchecked")
    public final <X> X configFor(final Class<X> klass) {

        final X config = (X)this.index.get(klass);

        if (config == null) throw new UncheckedException("Cannot find config for class: "+klass);

        return config;

    }

}
