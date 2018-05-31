/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.transferobject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This class exists because we need to distinguish between a null value and an undefined
 * value, and we can't rely on maps supporting null values as some do (HashMap) and some
 * don't (ConcurrentHashMap).
 */
public class Values {

    private final Set<String> keys = new HashSet<>();
    private final Map<String,Object> values = new HashMap<>();

    private final Map<String,Values> nested = new HashMap<>();
    private final Map<String,Set<Values>> collections = new HashMap<>();

    public Values() {
        super();
    }

    public Values(final Map<String,Object> values) {

        this();

        this.putAll(values);

    }

    public final void putAll(final Map<String,Object> values) {

        for (final Map.Entry<String,Object> entry : values.entrySet()) {
            this.put( // gives null tracking a chance to kick in
                    entry.getKey(),
                    entry.getValue());
        }

    }

    public void put(final String key, Object value) {
        this.keys.add(key);
        if (value != null) this.values.put(key,value);
    }

    public Object get(final String key) {
        return this.values.get(key);
    }

    public void putNested(final String key, Values nested) {
        this.keys.add(key);
        if (nested != null) this.nested.put(key,nested);
    }

    public void putCollections(final String key, Set<Values> collection) {
        this.keys.add(key);
        if (collection != null) this.collections.put(key,collection);
    }

    public Map<String,Object> values() {
        return new HashMap<>(this.values);
    }

    public Set<String> nulls() {
        final Set<String> nullValues = new HashSet<>(this.keys);
        nullValues.removeAll(this.values.keySet());
        nullValues.removeAll(this.nested.keySet());
        nullValues.removeAll(this.collections.keySet());
        return nullValues;
    }

    public Set<String> missing(final Set<String> allStringeys) {
        final Set<String> undefinedValues = new HashSet<>(allStringeys);
        undefinedValues.removeAll(this.keys);
        return undefinedValues;
    }

    public Map<String,Values> nesteds() {
        return new HashMap<>(this.nested);
    }

    public Map<String,Set<Values>> collections() {
        return new HashMap<>(this.collections);
    }

}
