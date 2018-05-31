/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.transferobject.impl;

import org.llaith.onyx.toolkit.transferobject.Values;
import org.llaith.onyx.toolkit.transferobject.ValuesExportAdapter;
import org.llaith.onyx.toolkit.transferobject.ValuesImportAdapter;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.reflection.FieldAccessUtil;
import org.llaith.onyx.toolkit.util.reflection.InstanceUtil;
import org.llaith.onyx.toolkit.util.reflection.PojoModel;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 *
 */
public class PojoTransferAdapter<T> implements ValuesImportAdapter<T>, ValuesExportAdapter<T> {

    private final Supplier<T> supplier;

    private final Map<String,PojoTransferAdapter<?>> nested = new HashMap<>();

    public PojoTransferAdapter(final Class<T> klass) {
        this(() -> InstanceUtil.newInstance(klass));
    }

    public PojoTransferAdapter(final Supplier<T> supplier) {
        this.supplier = Guard.notNull(supplier);
    }

    public PojoTransferAdapter<T> addSelfNestedAdapter(final String name) {
        return this.addNestedAdapter(name, this);
    }

    public PojoTransferAdapter<T> addNestedAdapter(final String name, final PojoTransferAdapter<T> adapter) {
        this.nested.put(name, adapter);
        return this;
    }

    public T importValues(final Values in) {

        final T target = this.supplier.get();

        // non-nested values
        for (final Map.Entry<String,Object> entry : in.values().entrySet()) {

            final String name = entry.getKey();
            final Object value = entry.getValue();

            // set it
            FieldAccessUtil.fieldSet(
                    target,
                    name,
                    value);

        }

        // nested values
        for (final Map.Entry<String,Values> entry : in.nesteds().entrySet()) {

            final String name = entry.getKey();
            final Values values = entry.getValue();

            FieldAccessUtil.fieldSet(
                    target,
                    name,
                    this.nested.get(name).importValues(values));

        }

        // nested collections
        for (final Map.Entry<String,Set<Values>> entry : in.collections().entrySet()) {

            final String name = entry.getKey();
            final Set<Values> set = entry.getValue();

            for (Values values : set) {

                FieldAccessUtil.fieldSet(
                        target,
                        name,
                        this.nested.get(name).importValues(values));

            }

        }

        // nulls
        for (final String name : in.nulls()) {

            // null it
            FieldAccessUtil.fieldSet(
                    target,
                    name,
                    null);

        }

        return target;

    }

    @Override
    public Values exportValues(T in) {

        final Values out = new Values();

        final PojoModel model = new PojoModel(in);

        // non nested values (incl nulls)
        for (final Field field : model.fields()) {

            // skip if it's nested.
            if (model.isNested(field.getName())) continue;

            // name
            final String name = field.getName();

            // get value
            final Object value = FieldAccessUtil.fieldGet(
                    in,
                    name);

            // put in export - incl null
            out.put(
                    name,
                    value);

        }

        // nested values (incl nulls)
        for (final Map.Entry<String,PojoModel> entry : model.getNestedModels().entrySet()) {

            final String name = entry.getKey();

            final Object value = FieldAccessUtil.fieldGet(
                    in,
                    name);

            @SuppressWarnings("unchecked")
            final Values nested = (value != null) ?
                    ((PojoTransferAdapter)this.nested.get(name)).exportValues(value) :
                    null;

            out.putNested(
                    name,
                    nested);

        }

        // nested collections (incl nulls)
        for (final Map.Entry<String,PojoModel> entry : model.getNestedCollections().entrySet()) {

            final String name = entry.getKey();

            final Collection<?> collection = (Collection<?>)FieldAccessUtil.fieldGet(
                    in,
                    name);

            if (collection == null) {

                out.putCollections(
                        name,
                        null);

            } else {

                @SuppressWarnings("unchecked")
                final Set<Values> cv = collection
                        .stream()
                        .map(nm -> ((PojoTransferAdapter)this.nested.get(name)).exportValues(nm))
                        .collect(Collectors.toSet());

                out.putCollections(
                        name,
                        cv);

            }

        }

        return out;

    }

}
