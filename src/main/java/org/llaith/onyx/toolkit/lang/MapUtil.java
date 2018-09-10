/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Used when wanting to toString an anonymous implementation of the Map *interface* where the implementor (a 3rd party)
 * has not implemented the toString themselves.
 */
public class MapUtil {

    @SuppressWarnings("squid:S1698")
    public static <K, V> String toString(final Map<K,V> map) {

        final Iterator<Map.Entry<K,V>> i = map.entrySet().iterator();

        if (!i.hasNext()) return "{}";

        final StringBuilder sb = new StringBuilder().append('{');

        while (true) {

            final Map.Entry<K,V> e = i.next();
            final K key = e.getKey();
            final V value = e.getValue();

            sb.append(key == map ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == map ? "(this Map)" : value);

            if (!i.hasNext()) return sb.append('}').toString();

            sb.append(',').append(' ');

        }

    }

    @SuppressWarnings("squid:S1698")
    public static <K, V> String toStringAlt(final Map<K,V> map) {

        final Iterator<K> i = map.keySet().iterator();

        if (!i.hasNext()) return "{}";

        final StringBuilder sb = new StringBuilder().append('{');

        while (true) {

            final K key = i.next();
            final V value = map.get(key);

            sb.append(key == map ? "(this Map)" : key);
            sb.append('=');
            sb.append(value == map ? "(this Map)" : value);

            if (!i.hasNext()) return sb.append('}').toString();

            sb.append(',').append(' ');

        }

    }

    // warning, will overwrite non-unique values when expressed as keys
    public static <K, V> Map<V,K> reverse(final Map<K,V> map) {

        return map.entrySet()
                  .stream()
                  .collect(Collectors.toMap(
                          Map.Entry::getValue,
                          Map.Entry::getKey));
        
    }

}
