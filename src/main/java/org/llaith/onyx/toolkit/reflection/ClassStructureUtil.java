/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection;

import com.google.common.collect.Lists;
import org.llaith.onyx.toolkit.lang.Guard;
import org.llaith.onyx.toolkit.lang.StringUtil;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: nos
 * Date: 03/04/12
 * Time: 11:59
 */
public final class ClassStructureUtil {

    private ClassStructureUtil() {super();}

    public static Set<Class<?>> findSuperTypes(final Class<?> klass) {
        return findSuperInterfaces(findSuperClasses(Guard.notNull(klass)));
    }

    public static Set<Class<?>> findSuperClasses(final Class<?> klass) {
        final Set<Class<?>> s = new HashSet<>();
        Class<?> c = klass;
        while (c != null) {
            s.add(c);
            c = c.getSuperclass();
        }
        return s;
    }

    public static Set<Class<?>> findSuperInterfaces(final Set<Class<?>> set) {
        final Deque<Class<?>> in = new ArrayDeque<>();
        in.addAll(set);
        final Set<Class<?>> out = new HashSet<>();
        while (!in.isEmpty()) {
            final Class<?> c = in.pop();
            out.add(c);
            if (c.getInterfaces() != null) Collections.addAll(in, c.getInterfaces());
        }
        return out;
    }

    /**
     * Can skip, Example: classStack(Some.class).push(Object.class);
     */
    public static Deque<Class<?>> classStack(final Class<?> klass) {

        // 
        // build ordered class
        final Deque<Class<?>> ordered = new ArrayDeque<>();

        Class<?> srch = klass;
        while (srch != Object.class) {
            ordered.push(srch);
            srch = srch.getSuperclass();
        }

        return ordered;

    }

    public static List<Field> declaredFieldsOf(final Class<?> startClass) {

        return declaredFieldsUpTo(
                startClass,
                Object.class);

    }

    // pass null as the second param to include object fields
    public static List<Field> declaredFieldsUpTo(final Class<?> startClass, final Class<?> exclusiveParent) {

        final List<Field> currentClassFields = Lists.newArrayList(startClass.getDeclaredFields());

        final Class<?> parentClass = startClass.getSuperclass();

        if (parentClass != null && (exclusiveParent == null || !(parentClass.equals(exclusiveParent)))) {

            final List<Field> parentClassFields = declaredFieldsUpTo(
                    parentClass,
                    exclusiveParent);

            currentClassFields.addAll(parentClassFields);
        }

        return currentClassFields;

    }

    public static boolean hasPackage(final String type) {
        return type.contains(".");
    }

    public static String packageFromName(final String type) {
        return !hasPackage(type) ? "" : type.substring(0, type.lastIndexOf('.'));
    }

    // this will break for canonical names
    public static String unqualifyClassName(final String type) {
        return !hasPackage(type) ? type : type.substring(type.lastIndexOf('.') + 1);
    }


    public static String packageFromClass(final Class<?> klass) {
        return packageFromName(Guard.notNull(klass.getName()));
    }

    public static String unqualifyClass(final Class<?> klass) {
        if (klass.isAnonymousClass()) return unqualifyClassName(klass.getName());
        return klass.getSimpleName();
    }

    public static String outerClassNameFor(final Class<?> klass) {
        final String fullName = Guard.notNull(klass.getName());

        final int pos = fullName.indexOf('$');
        if (pos >= 0) {
            return fullName.substring(0, pos);
        }

        return fullName;
    }

    public static String synthesiseReferenceFieldName(final Field first, final Field second) {

        final String className = StringUtil.lowercaseFirstLetter(
                unqualifyClass(
                        first.getType()));

        final String oppName = second.getName();

        return (oppName.startsWith(className)) ?
                oppName :
                className + StringUtil.uppercaseFirstLetter(oppName);


    }

    public static List<String> fieldsToNames(final Collection<Field> fields) {

        return fields.stream()
                     .map(e -> e.getName())
                     .collect(Collectors.toList());

    }

}
