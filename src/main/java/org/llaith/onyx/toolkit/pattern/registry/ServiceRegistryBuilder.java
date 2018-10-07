/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.pattern.registry;

import java.util.function.Supplier;

/**
 *
 */
public interface ServiceRegistryBuilder extends ServiceRegistry {

    <X> ServiceRegistryBuilder registerService(Class<X> contract, Supplier<X> provider);

}
