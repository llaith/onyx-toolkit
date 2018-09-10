/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.reflection;

import com.google.common.base.Defaults;
import org.llaith.onyx.toolkit.exception.UncheckedException;
import org.llaith.onyx.toolkit.lang.Guard;

import javax.annotation.Nullable;

/**
 * Don't forget there is also the Guava Primitives class.
 */
public class Primitive<T> {

    // http://stackoverflow.com/questions/1040868/java-syntax-and-meaning-behind-b1ef9157-binary-address
    public static final Primitive<Void> VOID = new Primitive<>(void.class, Void.class, null, null);
    public static final Primitive<Boolean> BOOLEAN = new Primitive<>(boolean.class, Boolean.class, "Z", false);
    public static final Primitive<Byte> BYTE = new Primitive<>(byte.class, Byte.class, "B", (byte)0);
    public static final Primitive<Character> CHAR = new Primitive<>(char.class, Character.class, "C", (char)0);
    public static final Primitive<Short> SHORT = new Primitive<>(short.class, Short.class, "S", (short)0);
    public static final Primitive<Integer> INT = new Primitive<>(int.class, Integer.class, "I", 0);
    public static final Primitive<Long> LONG = new Primitive<>(long.class, Long.class, "J", 0l);
    public static final Primitive<Float> FLOAT = new Primitive<>(float.class, Float.class, "F", 0.0f);
    public static final Primitive<Double> DOUBLE = new Primitive<>(double.class, Double.class, "D", 0.0);

    @SuppressWarnings("squid:S2386")
    public static final Primitive<?>[] primitives = {VOID, BOOLEAN, BYTE, CHAR, SHORT, INT, LONG, FLOAT, DOUBLE};

    private final Class<?> primitiveClass;
    private final Class<T> wrapperClass;
    private final String arrayClassSymbol;
    private final T initValue;

    Primitive(final Class<?> primitiveClass, final Class<T> wrapperClass, final String arrayClassSymbol, final T initValue) {
        this.primitiveClass = Guard.notNull(primitiveClass);
        this.wrapperClass = Guard.notNull(wrapperClass);
        this.arrayClassSymbol = arrayClassSymbol;
        this.initValue = initValue;
    }

    public Class<?> primitiveClass() {
        return primitiveClass;
    }

    public Class<T> wrapperClass() {
        return wrapperClass;
    }

    public String arrayClassSymbol() {
        return arrayClassSymbol;
    }

    public T initValue() {
        return initValue;
    }

    public static boolean isPrimitiveOrWrapperType(final Class<?> klass) {
        return (!isPrimitiveType(klass)) && (!isWrapperType(klass));
    }

    public static boolean isPrimitiveOrWrapperType(final String className) {
        return (!isPrimitiveType(className)) && (!isWrapperType(className));
    }

    public static boolean isPrimitiveType(final String className) {
        for (final Primitive<?> p : Primitive.primitives) {
            if (p.primitiveClass().getName().equals(className)) return true;
        }
        return false;
    }

    public static boolean isPrimitiveType(final Class<?> klass) {
        return klass.isPrimitive();
    }

    @Nullable
    public static Primitive<?> primitiveFor(final Class<?> type) {
        return primitiveForName(type.getName());
    }

    @Nullable
    public static Primitive<?> primitiveForName(final String className) {
        for (final Primitive<?> p : Primitive.primitives) {
            if (p.primitiveClass().getName().equals(className)) return p;
        }
        return null; // Expect() if you care
    }

    public static boolean isWrapperType(final String className) {
        for (final Primitive<?> p : Primitive.primitives) {
            if (p.wrapperClass().getName().equals(className)) return true;
        }
        return false;
    }

    public static boolean isWrapperType(final Class<?> clazz) {
        for (final Primitive<?> p : Primitive.primitives) {
            if (p.wrapperClass().getName().equals(clazz.getName())) return true;
        }
        return false;
    }

    public static Object defaultValue(final Class<?> clazz) {
        // TODO: Umm.. i'm just sort of moving this here for now, i don't want to forget the Defaults class but it
        // isn't needed here.
        if (!clazz.isPrimitive()) throw new UncheckedException(String.format(
                "Class of: %s is not a primitive",
                clazz.getName()));

        return Defaults.defaultValue(clazz);

    }

}
