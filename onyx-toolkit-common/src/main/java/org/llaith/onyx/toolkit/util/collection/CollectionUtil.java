/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.collection;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class CollectionUtil {

    public static <T> T head(final List<T> list) {

        if ((list != null) && (list.size() > 0)) return list.get(0);

        return null;

    }

    public static <T> List<T> tail(final List<T> list) {

        if ((list != null) && (list.size() > 0))
            return list.subList(1, list.size());

        return null;

    }

    public <X> Set<X> newLruSet(final int max) {

        return Collections.newSetFromMap(new LinkedHashMap<X,Boolean>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<X,Boolean> eldest) {
                return size() > max;
            }
        });

    }

}

