package org.llaith.onyx.toolkit.session;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 *
 */
public class SessionControl<C> {

    public interface SessionCallback<C, R> {

        R in(Session<C> session);

    }

    public interface SessionProvider<C> {

        <X> X newSession(Map<Class<?>,Function<C,?>> factories, SessionCallback<C,X> callback);

    }

    private final SessionProvider<C> handler;

    private final Map<Class<?>,Function<C,?>> factories = new HashMap<>();

    public SessionControl(final SessionProvider<C> handler, final Map<Class<?>,Function<C,?>> factories) {

        this.handler = Guard.notNull(handler);

        this.factories.putAll(Guard.notNullOrEmpty(factories));

    }

    public <X> X with(final SessionCallback<C,X> callback) {

        return handler.newSession(this.factories, callback);

    }

}
