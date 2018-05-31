package org.llaith.onyx.toolkit.util.reflection;


/**
 *
 */
/*
 * Copyright (c) 2016.
 */

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: nos
 * Date: 19/10/11
 * Time: 12:15
 */
public class AnnotationUtil {

    public static boolean hasAnnotationOf(final Class<?> klass, final Collection<Class<? extends Annotation>> wanted) {
        return hasAnnotationOf(klass.getAnnotations(), wanted);
    }

    public static boolean hasAnnotationOf(final Field field, final Collection<Class<? extends Annotation>> wanted) {
        return hasAnnotationOf(field.getAnnotations(), wanted);
    }

    public static boolean hasAnnotationOf(final Annotation[] found, final Collection<Class<? extends Annotation>> wanted) {
        if ((found == null) || (found.length < 1)) return false;
        if ((wanted == null) || (wanted.size() < 1)) return false;

        final Set<String> set = annotationNames(found).keySet();
        set.retainAll(InstanceUtil.classNamesFrom(wanted));
        return !set.isEmpty();
    }


    public static List<Annotation> excludeAnnotations(@Nullable final Annotation[] found, @Nullable final Set<Class<? extends Annotation>> ignored) {
        if ((found == null) || (found.length < 1)) return new ArrayList<>();
        if ((ignored == null) || (ignored.size() < 1)) return new ArrayList<>();

        final Map<String,Annotation> map = annotationNames(found);
        map.keySet().removeAll(InstanceUtil.classNamesFrom(ignored));

        final List<Annotation> ret = new ArrayList<>();

        for (final Map.Entry<String,Annotation> entry : map.entrySet()) {
            ret.add(entry.getValue());
        }

        return ret;
    }


    public static Map<String,Annotation> annotationNames(final Annotation[] found) {
        final Map<String,Annotation> ret = new HashMap<>();
        for (final Annotation a : found) {
            // NOTE: getClass doesn't work for annotations! Also, retainAll on Classes donesn't work (classloaders?)
            ret.put(InstanceUtil.getReflectionName(a.annotationType()), a);
        }
        return ret;
    }


    public static Set<Annotation> checkAnnotationsPresent(final Class<?> klass, final Collection<Class<? extends Annotation>> wanted) {
        final Set<Class<? extends Annotation>> have = collectAnnotationTypes(klass);
        final Set<Annotation> found = new HashSet<>();
        for (final Class<? extends Annotation> ann : wanted) {
            if (have.contains(ann)) found.add(klass.getAnnotation(ann));
        }
        return found;
    }


    public static Set<Class<? extends Annotation>> collectAnnotationTypes(final Class<?> klass) {
        final Set<Class<? extends Annotation>> found = new HashSet<>();
        for (final Annotation ann : klass.getAnnotations()) {
            found.add(ann.annotationType());
        }
        return found;
    }


    public static Set<Annotation> checkAnnotationsPresent(@Nonnull final Field field, final Collection<Class<? extends Annotation>> wanted) {
        final Set<Class<? extends Annotation>> have = collectAnnotationTypes(field);
        final Set<Annotation> found = new HashSet<>();
        for (final Class<? extends Annotation> ann : wanted) {
            if (have.contains(ann)) found.add(field.getAnnotation(ann));
        }
        return found;
    }


    public static Set<Class<? extends Annotation>> collectAnnotationTypes(final Field klass) {
        final Set<Class<? extends Annotation>> found = new HashSet<>();
        for (final Annotation ann : klass.getAnnotations()) {
            found.add(ann.annotationType());
        }
        return found;
    }


    public static Set<Annotation> checkAnnotationsPresent(final Method method, final Collection<Class<? extends Annotation>> wanted) {
        final Set<Class<? extends Annotation>> have = collectAnnotationTypes(method);
        final Set<Annotation> found = new HashSet<>();
        for (final Class<? extends Annotation> ann : wanted) {
            if (have.contains(ann)) found.add(method.getAnnotation(ann));
        }
        return found;
    }


    public static Set<Class<? extends Annotation>> collectAnnotationTypes(final Method method) {
        final Set<Class<? extends Annotation>> found = new HashSet<>();
        for (final Annotation ann : method.getAnnotations()) {
            found.add(ann.annotationType());
        }
        return found;
    }


    public static Map<String,Object> extractDefaultValues(final Class<? extends Annotation> klass) {

        final Map<String,Object> map = new HashMap<>();

        for (final Method m : klass.getMethods()) {
            map.put(m.getName(), m.getDefaultValue());
        }

        return map;

    }

    public static <X extends Annotation> X newAnnotation(final Class<X> klass) {
        return newAnnotation(klass, new HashMap<>());
    }


    public static <X extends Annotation> X newAnnotation(final Class<X> klass, final Object val) {
        final Map<String,Object> values = new HashMap<>();
        values.put("value", val);
        return newAnnotation(klass, values);
    }


    @SuppressWarnings("unchecked")
    // BLOG THIS
    public static <X extends Annotation> X newAnnotation(final Class<X> klass, final Map<String,Object> vals) {

        final Map<String,Object> map = extractDefaultValues(klass);
        map.putAll(vals);

        return (X)Proxy.newProxyInstance(
                klass.getClassLoader(),
                new Class<?>[] {klass},
                (proxy, method, args) -> {
                    if ("annotationType".equals(method.getName())) return klass;
                    else return map.get(method.getName());
                });

    }

    public static List<Field> fieldsMarkedWith(final Class<?> klass, final Class<? extends Annotation> annClass) {

        return fieldsMarkedWithAny(klass, Collections.singletonList(annClass));

    }

    public static List<Field> fieldsMarkedWithAny(final Class<?> klass, final List<Class<? extends Annotation>> annClasses) {

        final List<Field> fields = new ArrayList<>();

        Class<?> srch = klass;

        while (srch != null) {

            for (final Field field : srch.getDeclaredFields()) {
                for (final Class<? extends Annotation> annClass : annClasses) {
                    if (field.isAnnotationPresent(annClass)) {
                        fields.add(field);
                        break;
                    }
                }
            }

            srch = srch.getSuperclass();

        }

        return fields;

    }

    public static List<Field> fieldsMarkedWithAll(final Class<?> klass, final List<Class<? extends Annotation>> annClasses) {

        final List<Field> fields = new ArrayList<>();

        for (final Field field : klass.getDeclaredFields()) {
            boolean ok = true;
            for (final Class<? extends Annotation> annClass : annClasses) {
                if (!field.isAnnotationPresent(annClass)) {
                    break;
                }
            }
            fields.add(field);
        }

        return fields;

    }

}
