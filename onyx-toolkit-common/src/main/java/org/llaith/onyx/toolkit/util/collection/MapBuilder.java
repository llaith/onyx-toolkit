/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.collection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 */
public class MapBuilder<K, V> {

    private final Map<K,V> delegate;

    public static <X, Y> MapBuilder<X,Y> empty() {
        return wrap(new HashMap<>());
    }

    public static <X, Y> MapBuilder<X,Y> copy(final Map<X,Y> other) {
        return wrap(new HashMap<>(other));
    }

    public static <X, Y> MapBuilder<X,Y> wrap(final Map<X,Y> other) {
        return new MapBuilder<>(other);
    }

    private MapBuilder(final Map<K,V> other) {
        this.delegate = other;
    }

    public MapBuilder<K,V> put(final K key, final V value) {

        this.delegate.put(key, value);

        return this;

    }

    public MapBuilder<K,V> putAll(final Map<K,V> other) {

        this.delegate.putAll(other);

        return this;

    }

    public MapBuilder<K,V> retainKeys(final Collection<K> keys) {

        this.delegate.keySet().retainAll(keys);

        return this;

    }

    public MapBuilder<K,V> removeKeys(final Collection<K> keys) {

        this.delegate.keySet().removeAll(keys);

        return this;

    }

    public ListBuilder<V> valuesToList() {

        return ListBuilder.copy(this.delegate.values());

    }

    public SetBuilder<V> valuesToSet() {

        return SetBuilder.copy(this.delegate.values());

    }

    public Collection<V> valuesAsCollection() {

        return this.delegate.values();

    }

    public Map<K,V> asMap() {

        return this.delegate;

    }

    public Map<K,V> toMap() {

        return new HashMap<>(this.delegate);

    }

    public Stream<Map.Entry<K,V>> asStream() {

        return this.delegate.entrySet().stream();

    }

}
