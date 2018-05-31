/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;

import com.google.common.base.Defaults;
import org.llaith.onyx.toolkit.util.lang.Guard;
import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.lang.reflect.Field;
import java.util.function.BiFunction;

/**
 * Use the Fest-reflect or perhaps the vidageek.TypeMirror in preference to this, but sometimes
 * you want to set properties as objects and that's what this is for.
 */
public class FieldAccessUtil {

    @SuppressWarnings("squid:S1166")
    public static void fieldSet(final Object target, final String fieldName, final Object value) {

        Class<?> klass = Guard.notNull(target).getClass();

        while (klass != null) {
            try {

                final Field field = klass.getDeclaredField(fieldName);

                field.setAccessible(true);

                if ((field.getType().isPrimitive()) && (value == null)) {

                    field.set(
                            target,
                            Defaults.defaultValue(field.getType()));

                } else {

                    field.set(
                            target,
                            value);

                }

                return;

            } catch (NoSuchFieldException e) {

                klass = klass.getSuperclass();

            } catch (Exception e) {

                throw UncheckedException.wrap(String.format(
                        "Cannot set field value: %s on field named: %s in object: %s.",
                        value,
                        fieldName,
                        klass.getName()
                ), e);

            }
        }

        throw new UncheckedException(String.format(
                "Cannot find field named: %s in object: %s.",
                fieldName,
                target.getClass().getName()));

    }

    @SuppressWarnings({"unchecked", "squid:S1166"})
    public static Object fieldGet(final Object target, final String fieldName) {

        return fieldGet(target, fieldName, (f, o) -> o);

    }

    @SuppressWarnings({"unchecked", "squid:S1166"})
    public static Object fieldGet(final Object target, final String fieldName, final BiFunction<Field,Object,Object> reader) {

        Guard.notNull(reader);

        Class<?> klass = Guard.notNull(target.getClass());

        while (klass != null) {

            try {

                final Field field = klass.getDeclaredField(fieldName);

                field.setAccessible(true); // should i set this back?

                final Object value = field.get(target);

                if ((field.getType().isPrimitive()) && (value == null)) {
                    return Defaults.defaultValue(field.getType());
                }

                return reader.apply(field, value);

            } catch (NoSuchFieldException e) {

                klass = klass.getSuperclass();

            } catch (Exception e) {

                throw UncheckedException.wrap(String.format(
                        "Cannot get value from field named: %s in object of: %s.",
                        fieldName,
                        klass.getName()
                ), e);

            }

        }

        throw new UncheckedException(String.format(
                "Cannot find field named: %s in object of: %s.",
                fieldName,
                target.getClass().getName()));

    }

    public static <X> X fieldGet(final Object target, final Class<X> fieldClass, final String fieldName) {

        final Object val = fieldGet(target, fieldName);

        if (val == null) return null;

        if (fieldClass.isAssignableFrom(val.getClass())) return fieldClass.cast(val);

        throw new ClassCastException(String.format(
                "Class of field named: %s in object of: %s is of type: %s but we expected a type of: %s.",
                fieldName,
                target.getClass().getName(),
                val.getClass(),
                fieldClass));

    }

    public static <T> void copyFields(T target, T source) throws IllegalAccessException {

        final Class<?> clazz = source.getClass();

        for (final Field field : clazz.getFields()) {
            final Object value = field.get(source);
            field.set(target, value);
        }

    }

}
