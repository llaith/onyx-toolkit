/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;

import com.google.common.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 *
 */
public class InstanceUtil {

    private static final Logger logger = LoggerFactory.getLogger(InstanceUtil.class);

    @SuppressWarnings("unchecked")
    public static <X> X newInstanceForName(final String name) {
        return (X)newInstance(forName(name));
    }

    public static <X> X newInstance(final Class<? extends X> klass) {
        try {
            return klass.getConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            throw UncheckedException.wrap(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <X> X newInstance(final TypeToken<X> token) {
        try {
            return (X)token.getRawType().getConstructor().newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | InstantiationException e) {
            throw UncheckedException.wrap(e);
        }
    }

    @SafeVarargs
    public static <X> List<X> newInstances(final Class<X>... klasses) {
        return InstanceUtil.newInstances(Arrays.asList(klasses));
    }

    public static <X> List<X> newInstances(final Collection<Class<X>> klasses) {
        final List<X> instances = new ArrayList<>();

        for (final Class<? extends X> klass : klasses) {
            instances.add(InstanceUtil.newInstance(klass));
        }

        return instances;
    }

    public static Set<String> classNamesFrom(final Collection<? extends Class<?>> classes) {
        final Set<String> ret = new HashSet<>();
        for (final Class<?> c : classes) {
            ret.add(c.getName());
        }
        return ret;
    }

    public static <X> List<Class<X>> forNames(final String... names) {
        return InstanceUtil.forNames(Arrays.asList(names));
    }

    public static <X> List<Class<X>> forNames(final Collection<String> names) {
        final List<Class<X>> classes = new ArrayList<>();

        for (final String name : names) {
            classes.add(InstanceUtil.<X>forName(name));
        }

        return classes;
    }

    @SuppressWarnings("unchecked")
    public static <X> Class<X> forName(final String name) {
        try {
            // happy to get a ClassCastException.
            return (Class<X>)Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw UncheckedException.wrap(e);
        }
    }

    /*
     * NOTE: The canonical name format is *evil*. It looses the package (probably because the
     * member classes use '.' instead of '$'. So when using string representations, we can't split out
     * the package. For this reason, invent a format called 'ReflectionName' which is getCanonicalName()
     * for arrays, and getName() for everything else. Any other exceptions or quirks will be dealt
     * with in here.
     * ReflectionName format is used extensively in the codegen project.
     */
    public static String getReflectionName(final Class<?> type) {

        // if it's an array we return the canonical name
        if (type.isArray()) return type.getCanonicalName();

        // else we return the Class.forName() compatible name
        return type.getName();

    }

    public static Class<?> forReflectionName(final String name) throws ClassNotFoundException {

        // if name endswith [] it's an array that has been get[Canonical/Simple]Named().
        if (name.endsWith("[]")) return arrayForCanonicalName(name);

        // try as a primitive
        final Primitive<?> p = Primitive.primitiveForName(name);
        if (p != null) return p.primitiveClass();

        // else we start the canonical name dance.
        return forCanonicalName(name);
    }

    // Needs to 'guess' nested classes. Canonical names are evil, makes Nested classes look
    // like packages, ruining unqualifying them.
    public static Class<?> forCanonicalName(final String name) throws ClassNotFoundException {

        // the problem is if we getCanonicalName() instead of getName() we loose the '$" on nested.
        // but it's safe to replace them backwards as you can't have a $.$
        String test = name;
        while (true) {
            try {
                return Class.forName(test);
            } catch (ClassNotFoundException e) {
                logger.debug("Ignoring exception, ", e);
            }
            final int pos = test.lastIndexOf('.');
            if (pos >= 0) {
                test = test.substring(0, pos) + "$" + test.substring(pos + 1);
            } else {
                // throw the original error
                Class.forName(name);
            }
        }
    }

    public static Class<?> arrayForCanonicalName(final String name) throws ClassNotFoundException {
        return Class.forName(arrayCanonicalNameToArrayName(name));
    }

    public static String arrayCanonicalNameToArrayName(final String name) {

        int depth = 0;
        String s = new StringBuffer(name).reverse().toString();

        while (true) {

            if (s.startsWith("][")) {
                depth++;
                s = s.substring(2);
            } else {
                final StringBuilder out = new StringBuilder();
                for (int i = 0; i < depth; i++) {
                    out.append("[");
                }
                s = new StringBuffer(s).reverse().toString();
                final Primitive<?> p = Primitive.primitiveForName(s);
                if (p != null) {
                    out.append(p.arrayClassSymbol());
                } else {
                    out.append("L");
                    out.append(s);
                    out.append(";"); // weird but true
                }
                return out.toString();
            }
        }

    }

    // from here: http://jroller.com/eyallupu/entry/two_side_notes_about_arrays
    public static Object createNativeArray(final String typeName, final int... dim) throws ClassNotFoundException {

        final Class<?> clazz;
        if ("byte".equals(typeName)) {
            clazz = Byte.TYPE;
        } else if ("char".equals(typeName)) {
            clazz = Character.TYPE;
        } else if ("double".equals(typeName)) {
            clazz = Double.TYPE;
        } else if ("float".equals(typeName)) {
            clazz = Float.TYPE;
        } else if ("int".equals(typeName)) {
            clazz = Integer.TYPE;
        } else if ("long".equals(typeName)) {
            clazz = Long.TYPE;
        } else if ("short".equals(typeName)) {
            clazz = Short.TYPE;
        } else if ("boolean".equals(typeName)) {
            clazz = Boolean.TYPE;
        } else {
            throw new ClassNotFoundException(typeName);
        }

        return Array.newInstance(clazz, dim);
        
    }

}
