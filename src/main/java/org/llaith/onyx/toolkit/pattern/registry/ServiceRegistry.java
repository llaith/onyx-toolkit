/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.registry;

/**
 * Note class was quickly converted from ImmutableRegistry to allow supply (delayed construction) in the face of
 * dropwizards delayed jersey implementation. It should have more thought put in to a more specific interface.
 */
public interface ServiceRegistry {

    boolean hasServiceFor(Class<?> contract);

    <X> X serviceFor(Class<X> contract);

}
