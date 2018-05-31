/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.dto;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class DtoBuilder {

    private final String name;
    private final Map<String,DtoField> fieldIndex = new HashMap<>();

    public DtoBuilder(final String name) {
        this(name,null);
    }

    public DtoBuilder(final String name, final Collection<DtoField> fields) {
        this.name = name;
        if (fields != null) for (DtoField field : fields) {
            this.fieldIndex.put(field.name(), field);
        }
    }

    /*
     * I've added this for post construction adjustment instead of modifying the DT itself or having lots of metadata
     * to adjust the build,
     */
    public DtoBuilder removeAttribute(final String id) {
        this.fieldIndex.remove(id);
        return this;
    }

    /*
     * Used when adding fields that have been manually constructed.
     */
    public DtoBuilder addField(final DtoField field) {
        this.fieldIndex.put(field.name(), field);
        return this;
    }

    public Dto build() {

        return new Dto(
                this.name,
                this.fieldIndex.values());

    }

}
