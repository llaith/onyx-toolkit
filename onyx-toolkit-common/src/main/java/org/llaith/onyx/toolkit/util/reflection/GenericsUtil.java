/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;

import org.llaith.onyx.toolkit.util.lang.Guard;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * There are more of these in other projects to go in.
 */
public class GenericsUtil {

    public static Class<?> concreteElementTypeOfCollection(final Field field) {
        return concreteElementTypeOfCollection(Guard.notNull(field)
                                                    .getGenericType());
    }

    public static Class<?> concreteElementTypeOfCollection(final Type collectionType) {
        Guard.notNull(collectionType);

        if (!(collectionType instanceof ParameterizedType)) return null;

        final ParameterizedType pt = (ParameterizedType)collectionType;
        if ((pt.getActualTypeArguments() == null) || (pt.getActualTypeArguments().length < 1)) return null;
        final Type arg = pt.getActualTypeArguments()[0];

        if (!(arg instanceof Class)) return null;
        return (Class<?>)arg;
    }

    public static Class<?> genericParent(final Class<?> target, final int arg) {
        return (Class<?>)((ParameterizedType)target
                .getGenericSuperclass())
                .getActualTypeArguments()[arg];
    }


}
