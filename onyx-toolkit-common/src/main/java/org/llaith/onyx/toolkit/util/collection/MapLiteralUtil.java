/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.collection;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// largely from boon, need to think about how to proceed, boon, my own, or guava.
public class MapLiteralUtil {

    public static <K, V> Map<K,V> emptyMap() {
        return Collections.emptyMap();
    }

    public static <K, V> Map<K,V> mapOf(K k0, V v0) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(K k0, V v0, K k1, V v1) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        return map;
    }


    public static <K, V> Map<K,V> mapOf(K k0, V v0, K k1, V v1, K k2, V v2) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9) {
        Map<K,V> map = new LinkedHashMap<>(10);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10) {
        Map<K,V> map = new LinkedHashMap<>(11);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);

        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11) {
        Map<K,V> map = new LinkedHashMap<>(12);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);

        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12) {
        Map<K,V> map = new LinkedHashMap<>(13);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13) {
        Map<K,V> map = new LinkedHashMap<>(14);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14) {
        Map<K,V> map = new LinkedHashMap<>(15);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);

        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14, K k15, V v15) {
        Map<K,V> map = new LinkedHashMap<>(16);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);
        map.put(k15, v15);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14, K k15, V v15, K k16, V v16) {
        Map<K,V> map = new LinkedHashMap<>(17);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);
        map.put(k15, v15);
        map.put(k16, v16);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14, K k15, V v15, K k16, V v16, K k17, V v17) {
        Map<K,V> map = new LinkedHashMap<>(18);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);
        map.put(k15, v15);
        map.put(k16, v16);
        map.put(k17, v17);


        return map;
    }

    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14, K k15, V v15, K k16, V v16, K k17, V v17,
            K k18, V v18) {
        Map<K,V> map = new LinkedHashMap<>(19);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);
        map.put(k15, v15);
        map.put(k16, v16);
        map.put(k17, v17);
        map.put(k18, v18);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(
            K k0, V v0, K k1, V v1, K k2, V v2, K k3,
            V v3, K k4, V v4, K k5, V v5, K k6, V v6, K k7, V v7, K k8, V v8,
            K k9, V v9, K k10, V v10, K k11, V v11, K k12, V v12, K k13, V v13,
            K k14, V v14, K k15, V v15, K k16, V v16, K k17, V v17,
            K k18, V v18, K k19, V v19) {
        Map<K,V> map = new LinkedHashMap<>(20);
        map.put(k0, v0);
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        map.put(k6, v6);
        map.put(k7, v7);
        map.put(k8, v8);
        map.put(k9, v9);
        map.put(k10, v10);
        map.put(k11, v11);
        map.put(k12, v12);
        map.put(k13, v13);
        map.put(k14, v14);
        map.put(k15, v15);
        map.put(k16, v16);
        map.put(k17, v17);
        map.put(k18, v18);
        map.put(k19, v19);

        return map;
    }


    public static <K, V> Map<K,V> mapOf(List<K> keys, List<V> values) {
        Map<K,V> map = new LinkedHashMap<>(10 + keys.size());
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }


    public static <K, V> Map<K,V> mapOf(LinkedHashSet<K> keys, LinkedHashSet<V> values) {
        Map<K,V> map = new LinkedHashMap<>(10 + keys.size());
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    /**
     * Note, you need to make sure that the iterators are from some sort of ordered collection.
     *
     */
    public static <K, V> Map<K,V> mapOf(Iterable<K> keys, Iterable<V> values) {
        Map<K,V> map = new LinkedHashMap<>();
        Iterator<V> iterator = values.iterator();
        for (K k : keys) {
            if (iterator.hasNext()) {
                V v = iterator.next();
                map.put(k, v);
            } else {
                map.put(k, null);
            }
        }
        return map;
    }

    public static <K, V> Map<K,V> mapOf(K[] keys, V[] values) {

        Map<K,V> map = new LinkedHashMap<>(10 + keys.length);
        int index = 0;
        for (K k : keys) {
            if (index < keys.length) {
                V v = values[index];
                map.put(k, v);
            } else {
                map.put(k, null);
            }
            index++;
        }
        return map;
    }


    @SafeVarargs
    public static <K, V> Map<K,V> mapOf(Entry<K,V>... entries) {
        Map<K,V> map = new LinkedHashMap<>(entries.length);
        for (Entry<K,V> entry : entries) {
            map.put(entry.getKey(), entry.getValue());
        }
        return map;
    }

}
