/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 *
 */
public class Guard {

    public static void not(final boolean b) {
        if (!b) throw new IllegalStateException("Value cannot be false.");
    }

    @Nonnull
    public static <X> X notNull(@Nullable final X o) {
        if (o == null) throw new IllegalArgumentException("Value cannot be null.");
        return o;
    }

    @Nullable
    public static <X> X isNull(@Nullable final X o) {
        if (o != null) throw new IllegalArgumentException("Value must be null.");
        return null;
    }

    @Nonnull
    public static <X> X ifNull(@Nullable final X val, @Nonnull final X defaultVal) {

        return val == null ? defaultVal : val;

    }

    public static void noneNull(@Nullable final Object[] oa) {

        if (oa == null) throw new IllegalArgumentException("Value cannot be null.");

        for (int i = 0; i < oa.length; i++) {

            if (oa[i] == null) throw new IllegalArgumentException(
                    String.format("Value at index %s cannot be null.", i));

        }

    }

    @Nullable
    public static String blankToNull(@Nullable final String s) {
        if (StringUtil.isBlankOrNull(s)) return null;
        return s;
    }

    @Nonnull
    public static String nullToBlank(@Nullable final String s) {
        if (StringUtil.isBlankOrNull(s)) return "";
        return s;
    }

    @Nonnull
    public static String defaultIfNullOrBlank(@Nullable final String s, @Nonnull final String defaultStr) {
        return Guard.blankToNull(s) != null ?
                notNull(s) :
                notNull(defaultStr);
    }

    @Nullable
    public static <T> T[] emptyToNull(@Nullable final T[] arr) {
        if (arr == null) return null;
        if (arr.length < 1) return null;
        return arr;
    }

    @Nullable
    public static <T> Collection<T> emptyToNull(@Nullable final Collection<T> c) {
        if (c == null) return null;
        if (c.isEmpty()) return null;
        return c;
    }

    @Nonnull
    public static <T> Collection<T> nullToEmpty(@Nullable final Collection<T> it) {
        if (it != null) return it;
        return Collections.emptyList();
    }

    @Nonnull
    public static <T> Iterable<T> nullToEmpty(@Nullable final Iterable<T> it) {
        if (it != null) return it;
        return Collections.emptyList();
    }

    @Nonnull
    public static <K, V> Map<K,V> nullToEmpty(@Nullable final Map<K,V> it) {
        if (it != null) return it;
        return Collections.emptyMap();
    }

    @SuppressWarnings("unchecked")
    @Nonnull
    public static <T> T[] nullToEmpty(@Nullable final T[] arr) {
        if (arr == null) return (T[])new ArrayList<T>().toArray();
        return arr;
    }

    @Nullable
    public static <T> List<T> toListOrNull(@Nullable final T[] arr) {
        if (arr == null) return null;
        return Arrays.asList(arr);
    }

    @Nonnull
    public static <T> List<T> toListOrEmpty(@Nullable final T[] arr) {
        if (arr == null) return new ArrayList<>();
        return Arrays.asList(arr);
    }

    @Nonnull
    public static <T> List<T> toEmptyIfNull(@Nullable final List<T> list) {
        if (list == null) return new ArrayList<>(1);
        return list;
    }

    @Nonnull
    public static <K, V> Map<K,V> toEmptyIfNull(@Nullable final Map<K,V> map) {
        if (map == null) return new HashMap<>();
        return map;
    }

    @Nonnull
    public static <X> Stream<X> stream(@Nullable final Collection<X> source) {
        return Guard.nullToEmpty(source).stream();
    }

    @Nullable
    public static String notBlank(@Nullable final String s) {

        if (s == null) return null;

        if (s.trim().length() < 1)
            throw new IllegalArgumentException("String value cannot be empty or only whitespace.");

        return s;
    }

    @Nonnull
    public static String notBlankOrNull(@Nullable final String s) {

        return notNull(notBlank(s));

    }

    @Nonnull
    public static <T> T[] notNullOrEmpty(@Nullable final T[] arr) {
        if (arr == null || arr.length < 1) throw new IllegalArgumentException(
                "Array value cannot be null or empty.");
        return arr;
    }

    @Nonnull
    public static <T, C extends Collection<T>> C notNullOrEmpty(@Nullable final C collection) {
        if (collection == null || collection.isEmpty()) throw new IllegalArgumentException(
                "Collection value cannot be null or empty.");
        return collection;
    }

    @Nonnull
    public static <K, V, M extends Map<K,V>> M notNullOrEmpty(@Nullable final M collection) {
        if (collection == null || collection.isEmpty()) throw new IllegalArgumentException(
                "Map value cannot be null or empty.");
        return collection;
    }

    public static int notNegative(final int num) {
        if (num < 0) throw new IllegalArgumentException(
                "Int value cannot be negative.");

        return num;
    }

    public static int notNegativeOrZero(final int num) {
        if (num <= 0) throw new IllegalArgumentException(
                "Int value cannot be negative or zero.");

        return num;
    }

    public static Integer toIntegerOrNull(final String numStr) {

        // warning, don't try to autounbox this or it will cause a NPE

        try {

            return Integer.parseInt(numStr);

        } catch (NumberFormatException e) {

            return null;

        }

    }

    @Nonnull
    public static <X extends Annotation> Class<X> isRuntimeAnnotation(@Nullable final Class<X> injectionAnnotation) {

        if (Guard.notNull(injectionAnnotation).isAnnotationPresent(Retention.class) &&
                (injectionAnnotation.getAnnotation(Retention.class).value().equals(RetentionPolicy.RUNTIME))) {

            return injectionAnnotation;

        }

        throw new IllegalArgumentException(String.format("Injection Annotation is not runtime visible: %s", injectionAnnotation));

    }

    @Nullable
    public static <T> T expectObjectOf(@Nonnull final Class<T> klass, @Nullable final Object test) {

        if (test == null) return null;

        if (notNull(klass).isAssignableFrom(test.getClass())) throw new IllegalArgumentException(String.format(
                "Expected class that was assignable to: %s but was passed an object of class: %s",
                klass,
                test.getClass()));

        return klass.cast(test);

    }

    @Nullable
    public static <T> Class<T> expectObjectClassOf(@Nonnull final Class<T> klass, @Nullable final Object test) {

        if (test == null) return null;

        return expectClassOf(klass, test.getClass());

    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T> Class<T> expectClassOf(@Nonnull final Class<T> klass, @Nullable final Class<?> test) {

        if (test == null) return null;

        if (notNull(klass).isAssignableFrom(test)) throw new IllegalArgumentException(String.format(
                "Expected class that was assignable to: %s but was passed an object of class: %s",
                klass,
                test.getClass()));

        return (Class<T>)test;

    }

    public static <X> X defaultIfNull(final @Nullable X x, final @Nonnull X def) {

        return x != null ?
                x :
                def;

    }

}
