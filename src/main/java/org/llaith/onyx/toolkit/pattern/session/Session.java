package org.llaith.onyx.toolkit.pattern.session;

import org.llaith.onyx.toolkit.exception.UncheckedException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public class Session<C> {

    private final C connection;

    private final Map<Class<?>,Function<C,?>> factories;

    private final Map<Class<?>,Object> cache = new HashMap<>();

    public Session(Map<Class<?>,Function<C,?>> factories, final C connection) {

        this.factories = new HashMap<>(factories);

        this.connection = connection;

    }

    public <X> X use(final Class<X> klass) {

        if (!this.cache.containsKey(klass)) this.cache.put(
                klass,
                this.getFactory(klass)
                    .apply(this.connection));

        return klass.cast(this.cache.get(klass));

    }

    @SuppressWarnings("unchecked")
    private <Y, X extends Function<C,Y>> X getFactory(final Class<Y> klass) {

        if (!this.factories.containsKey(klass)) throw new UncheckedException(String.format(
                "Missing factory registration: %s",
                klass.getName()));

        return (X)this.factories.get(klass);

    }

}

