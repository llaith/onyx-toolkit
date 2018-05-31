/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.registry;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 *
 */
public class ServiceRegistryImpl implements ServiceRegistryBuilder {

    protected final Map<Class<?>,Supplier<?>> suppliers = new HashMap<>();

    @Override
    public boolean hasServiceFor(final Class<?> contract) {
        return this.suppliers.containsKey(contract);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X serviceFor(final Class<X> contract) {

        final X expected = (X)this.suppliers.get(contract);

        if (expected == null)
            throw new IllegalStateException("Missing service-supplier for contract: " + contract.getName());

        Object provider = this.suppliers.get(contract).get();

        if (provider == null)
            throw new IllegalStateException("Missing service-provider for contract: " + contract.getName());

        return contract.cast(provider);

    }

    @Override
    public <X> ServiceRegistryBuilder registerService(final Class<X> contract, final Supplier<X> supplier) {

        Guard.notNull(supplier);

        if (this.suppliers.containsKey(Guard.notNull(contract))) throw new IllegalStateException(String.format(
                "Cannot add service-factory of: %s to contract of: %s because the service-factory-supplier of: %s already is registered.",
                supplier.getClass().getName(),
                contract.getName(),
                this.suppliers.get(contract).getClass().getName()));

        this.suppliers.put(
                contract,
                supplier);

        return this;

    }

}
