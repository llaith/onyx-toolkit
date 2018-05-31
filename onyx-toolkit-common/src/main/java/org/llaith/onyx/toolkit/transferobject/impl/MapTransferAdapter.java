/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.transferobject.impl;

import com.google.common.base.Supplier;
import org.llaith.onyx.toolkit.transferobject.ValuesImportAdapter;
import org.llaith.onyx.toolkit.transferobject.Values;
import org.llaith.onyx.toolkit.transferobject.ValuesExportAdapter;
import org.llaith.onyx.toolkit.util.lang.Guard;

import java.util.Map;

/**
 *
 */
public class MapTransferAdapter implements ValuesImportAdapter<Map<String,Object>>, ValuesExportAdapter<Map<String,Object>> {

    public enum Mode {
        NORMAL, REMOVE_NULLS, RESET_IMPORT
    }

    private final Supplier<Map<String,Object>> supplier;
    private final Mode mode;

    public MapTransferAdapter(final Supplier<Map<String,Object>> supplier, final Mode mode) {
        this.supplier = Guard.notNull(supplier);
        this.mode = Guard.notNull(mode);
    }

    @Override
    public Map<String,Object> importValues(final Values in) {

        final Map<String,Object> out = this.supplier.get();

        if (Mode.RESET_IMPORT == this.mode) out.clear(); // the supplied one may not be blank!

        for (final Map.Entry<String,Object> entry : in.values().entrySet()) {

            final Object val = entry.getValue();

            // if we're clearing instead of setting nulls, continue
            if ((val == null) && (Mode.REMOVE_NULLS == this.mode)) continue;

            out.put(
                        entry.getKey(),
                        entry.getValue());

        }

        if (Mode.REMOVE_NULLS == this.mode) {

            for (final String key : in.nulls()) {
                out.remove(key);
            }

        }

        return out;

    }

    @Override
    public Values exportValues(final Map<String,Object> map) {

        throw new UnsupportedOperationException("Does not yet support nested models or collections");

        //return new TransferredValues(map);

    }

}
