package org.llaith.onyx.toolkit.util.reflection;

import java.lang.reflect.Type;

/**
 *
 */
public class ClassUtil {

    @SuppressWarnings("unchecked")
    public static <T> Class<T> determineClass(Class<? super T> bound, Type candidate) {
        
        if (candidate instanceof Class<?>) {
            final Class<?> cls = (Class<?>)candidate;
            if (bound.isAssignableFrom(cls)) {
                return (Class<T>)cls;
            }
        }

        return null;
    }

}
