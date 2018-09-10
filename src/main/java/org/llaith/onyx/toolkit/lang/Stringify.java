/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.lang;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Utility class to toString {@code Object}s to string using reflection and recursion.
 * <p>
 * From: http://stackoverflow.com/questions/3149951/java-tostring-tostringbuilder-not-sufficient-wont-traverse/20407041#20407041
 * <p>
 * I wanted an elegant solution to this problem that:
 * <p>
 * Does not use any external library
 * Uses Reflection to access fields, including superclass fields
 * Uses recursion to traverse the Object-graph with only one stack frame per call
 * Uses an IdentityHashMap to handle backwards references and avoid infinite recursion
 * Handles primitives, auto-boxing, CharSequences, enums, and nulls appropriately
 * Allows you to choose whether or not to parse static fields
 * Is simple enough to modify according to formatting preferences
 */
public class Stringify {

    /**
     * renamed to avoid static import conflicts, keeping old name for compatibility
     */
    public static String asString(Object object) {

        return toString(
                object,
                false,
                new IdentityHashMap<>(),
                0);

    }

    /**
     * renamed to avoid static import conflicts, keeping old name for compatibility
     */
    public static String asString(Object object, boolean isIncludingStatics) {

        return toString(
                object,
                isIncludingStatics,
                new IdentityHashMap<>(),
                0);

    }


    /**
     * Uses reflection and recursion to dump the contents of the given object using a custom, JSON-like notation (but not JSON). Does not format static fields.<p>
     *
     * @param object the {@code Object} to toString using reflection and recursion
     * @return a custom-formatted string representing the internal values of the parsed object
     * @see #toString(Object, boolean, IdentityHashMap, int)
     */
    public static String toString(Object object) {

        return toString(
                object,
                false,
                new IdentityHashMap<>(),
                0);

    }

    /**
     * Uses reflection and recursion to toString the contents of the given object using a custom, JSON-like notation (but not JSON).<p>
     * Parses all fields of the runtime class including super class fields, which are successively prefixed with "{@code super.}" at each level.<p>
     * {@code Number}s, {@code enum}s, and {@code null} references are formatted using the standard String.valueOf() method.
     * {@code CharSequences}s are wrapped with quotes.<p>
     * The recursive call invokes only one method on each recursive call, so limit of the object-graph depth is one-to-one with the stack overflow limit.<p>
     * Backwards references are tracked using a "visitor map" which is an instance of {@link IdentityHashMap}.
     * When an existing object reference is encountered the {@code "sysId"} is printed and the recursion ends.<p>
     *
     * @param object             the {@code Object} to toString using reflection and recursion
     * @param isIncludingStatics {@code true} if {@code static} fields should be dumped, {@code false} to skip them
     * @return a custom-formatted string representing the internal values of the parsed object
     */
    public static String toString(Object object, boolean isIncludingStatics) {

        return toString(
                object,
                isIncludingStatics,
                new IdentityHashMap<>(),
                0);

    }

    @SuppressWarnings({"squid:S138", "squid:S3776", "squid:MethodCyclomaticComplexity", "squid:S1067", "squid:S1166", "squid:S1181"})
    // special case
    private static String toString(
            final Object object,
            final boolean isIncludingStatics,
            final IdentityHashMap<Object,Object> visitorMap,
            final int tabCount) {

        if (object == null ||
                object instanceof Number ||
                object instanceof Character ||
                object instanceof Boolean ||
                object.getClass()
                      .isPrimitive() ||
                object.getClass()
                      .isEnum()) {
            return String.valueOf(object);
        }

        final StringBuilder builder = new StringBuilder();

        final int sysId = System.identityHashCode(object);

        if (object instanceof CharSequence) {

            builder
                    .append("\"")
                    .append(object)
                    .append("\"");

        } else if (visitorMap.containsKey(object)) {

            builder
                    .append("(sysId#")
                    .append(sysId)
                    .append(")");

        } else {

            visitorMap.put(
                    object,
                    object);

            final StringBuilder tabs = new StringBuilder();

            for (int t = 0; t < tabCount; t++) {
                tabs.append("\t");
            }

            if (object.getClass()
                      .isArray()) {

                builder
                        .append("[")
                        .append(object.getClass()
                                      .getName())
                        .append(":sysId#")
                        .append(sysId);

                int length = Array.getLength(object);
                for (int i = 0; i < length; i++) {
                    final Object arrayObject = Array.get(object, i);

                    final String dump = toString(
                            arrayObject,
                            isIncludingStatics,
                            visitorMap,
                            tabCount + 1);

                    builder
                            .append("\n\t")
                            .append(tabs)
                            .append("\"")
                            .append(i)
                            .append("\":")
                            .append(dump);
                }

                builder
                        .append(length == 0 ? "" : "\n")
                        .append(length == 0 ? "" : tabs)
                        .append("]");

            } else {

                // enumerate the desired fields of the object before accessing
                final TreeMap<String,Field> fieldMap = new TreeMap<>();  // can modify this to change or omit the sort order

                final StringBuilder superPrefix = new StringBuilder();
                for (Class<?> clazz = object.getClass(); clazz != null && !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
                    final Field[] fields = clazz.getDeclaredFields();
                    for (Field field : fields) {
                        if (isIncludingStatics || !Modifier.isStatic(field.getModifiers())) {
                            fieldMap.put(
                                    superPrefix + field.getName(),
                                    field);
                        }
                    }
                    superPrefix.append("super.");
                }

                builder.append("{")
                       .append(object.getClass()
                                     .getName())
                       .append(":sysId#")
                       .append(sysId);

                for (Map.Entry<String,Field> entry : fieldMap.entrySet()) {

                    final String name = entry.getKey();
                    final Field field = entry.getValue();
                    String dump;

                    try {
                        final boolean wasAccessible = field.isAccessible();
                        field.setAccessible(true);
                        final Object fieldObject = field.get(object);
                        field.setAccessible(wasAccessible);  // the accessibility flag should be restored to its prior ClassLoader state

                        dump = toString(
                                fieldObject,
                                isIncludingStatics,
                                visitorMap,
                                tabCount + 1);

                    } catch (Throwable e) {
                        dump = "!" + e.getClass()
                                      .getName() + ":" + e.getMessage();
                    }

                    builder
                            .append("\n\t")
                            .append(tabs)
                            .append("\"")
                            .append(name)
                            .append("\":")
                            .append(dump);

                }

                builder
                        .append(fieldMap.isEmpty() ? "" : "\n")
                        .append(fieldMap.isEmpty() ? "" : tabs)
                        .append("}");

            }
        }

        return builder.toString();
    }
}

