/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto.ext;

import com.google.common.base.Supplier;
import org.llaith.onyx.toolkit.dto.Dto;
import org.llaith.onyx.toolkit.dto.DtoCollection;
import org.llaith.onyx.toolkit.dto.DtoField;
import org.llaith.onyx.toolkit.transferobject.Values;
import org.llaith.onyx.toolkit.transferobject.ValuesExportAdapter;
import org.llaith.onyx.toolkit.transferobject.ValuesImportAdapter;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class DtoTransferAdapter implements ValuesImportAdapter<Dto>, ValuesExportAdapter<Dto> {

    private final Supplier<Dto> supplier;

    private final Map<String,DtoTransferAdapter> nested = new HashMap<>();

    public DtoTransferAdapter(final Supplier<Dto> supplier) {

        this.supplier = Guard.notNull(supplier);

    }

    public DtoTransferAdapter addSelfNestedAdapter(final String name) {
        return this.addNestedAdapter(name, this);
    }

    public DtoTransferAdapter addNestedAdapter(final String name, final DtoTransferAdapter adapter) {
        this.nested.put(name, adapter);
        return this;
    }

    public Dto importValues(final Values in) {

        final Dto dto = this.supplier.get();

        // non-nested values
        for (final Map.Entry<String,Object> entry : in.values().entrySet()) {
            dto.set(
                    entry.getKey(),
                    entry.getValue());
        }

        // nested values
        for (final Map.Entry<String,Values> entry : in.nesteds().entrySet()) {

            final String name = entry.getKey();
            final Values values = entry.getValue();

            dto.set(
                    name,
                    this.nested.get(name).importValues(values));

        }

        // nested collections
        for (final Map.Entry<String,Set<Values>> entry : in.collections().entrySet()) {

            final String name = entry.getKey();
            final Set<Values> set = entry.getValue();

            final DtoCollection dtoc = new DtoCollection();

            for (Values values : set) {

                dtoc.add(this.nested.get(name).importValues(values));

            }

            dto.set(
                    name,
                    dtoc);

        }

        // nulls (we don't need special treatment)
        for (final String id : in.nulls()) {
            dto.set(
                    id,
                    null);
        }

        return dto;

    }

    public Values exportValues(final Dto in) {

        final Values out = new Values();

        for (final Map.Entry<String,DtoField> entry : Guard.notNull(in).fields().entrySet()) {

            final String name = entry.getKey();
            final Class<?> type = entry.getValue().type();

            if (Dto.class.isAssignableFrom(type)) {

                final Dto nested = (Dto)entry.getValue().currentValue();

                //noinspection unchecked
                final Values values = (nested != null) ?
                        this.nested.get(name).exportValues(nested) :
                        null;

                out.putNested(
                        name,
                        values);

            } else if (DtoCollection.class.isAssignableFrom(type)) {

                final DtoCollection collection = (DtoCollection)entry.getValue().currentValue();

                if (collection == null) {

                    out.putCollections(
                            name,
                            null);

                } else {

                    final Set<Values> cv = new HashSet<>();

                    for (final Dto nd : collection) {

                        //noinspection unchecked
                        cv.add(this.nested.get(name).exportValues(nd));

                    }

                    out.putCollections(
                            name,
                            cv);

                }

            } else {
                out.put(
                        name,
                        entry.getValue().currentValue());
            }

        }

        return out;

    }


}
