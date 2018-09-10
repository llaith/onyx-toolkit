/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * Consider using Guava's Objects.toStringHelper instead. This is the very lazy
 * option, but has some value in cases where the trade-off (performance vs
 * forgetting to change the tostring) is worth it.
 */
public class ToStringUtil {

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ToStringIgnore {
    }

    public static String asString(final Object o) {
        // This is just easier and performance doesn't matter
        return (new ReflectionToStringBuilder(o) {
            @Override
            protected boolean accept(Field f) {
                return super.accept(f) && !f.isAnnotationPresent(ToStringIgnore.class);
            }
        }).toString();
    }


}
