/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.etc.context;

import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.util.HashMap;
import java.util.Map;

/**
 * An ultra-simple version of the Registry, for just storing impls. Designed for self registry,
 * and no-overwriting.
 */
public class Services {

    private final Map<Class<?>,Object> impls = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <X> X registerAndReturn(final X impl) {

        return this.registerAndReturn(
                (Class<X>)impl.getClass(),
                impl);

    }

    public <X> X registerAndReturn(final Class<X> klass, final X impl) {

        if (this.impls.containsKey(klass)) throw new UncheckedException(
                "Cannot register a duplicate implementation for class: "+klass);

        this.impls.put(klass,impl); // note impl.getClass() <> klass!

        return impl;

    }

    @SuppressWarnings("unchecked")
    public <X> Services register(final X impl) {

        return this.register(
                (Class<X>)impl.getClass(),
                impl);

    }

    public <X> Services register(final Class<X> klass, final X impl) {

        if (this.impls.containsKey(klass)) throw new UncheckedException(
                "Cannot register a duplicate implementation for class: "+klass);

        this.impls.put(klass,impl); // note impl.getClass() <> klass!

        return this;

    }

    @SuppressWarnings("unchecked")
    public <X> Services unregister(final Class<X> klass, final X impl) {

        if (!this.impls.containsKey(klass)) throw new UncheckedException(
                "Cannot unregister a missing implementation for class: "+klass);

        final X registered = (X)this.impls.get(klass);
        if (impl != registered) throw new UncheckedException(
                "Cannot unregister a service for which is registered to a different impl: "+registered);

        this.impls.remove(klass);

        return this;

    }

    public boolean hasServiceFor(final Class<?> klass) {
        return this.impls.containsKey(klass);
    }

    @SuppressWarnings("unchecked")
    public <X> X serviceFor(final Class<X> klass) {

        final X x = (X)this.impls.get(klass);

        if (x == null) throw new UncheckedException("Missing provider for class: "+klass.getName());

        return x;

    }

}
