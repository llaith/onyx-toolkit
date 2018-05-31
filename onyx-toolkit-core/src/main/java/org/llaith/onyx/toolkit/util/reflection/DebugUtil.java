/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;

import java.lang.Class;import java.lang.System;import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: nos
 * Date: 09/09/11
 * Time: 18:35
 */
public class DebugUtil {

    public static void dumpFieldAnnotations( final Class<?> klass) {
        for (final Field f : klass.getDeclaredFields()) {
            System.out.println("\t" + f.getName());
            for (final Annotation ann : f.getAnnotations()) {
                System.out.println("\t\t" + InstanceUtil.getReflectionName(ann.annotationType()));
            }
        }
    }

    public static void outputClassDecl(final String className) throws ClassNotFoundException {
        outputClassDecl(Class.forName(className));
    }

    public static void outputClassDecl(final Class<?> c) {
        System.out.format("Class:%n  %s%n%n",c.getName());
        System.out.format("Modifiers:%n  %s%n%n",
                          Modifier.toString(c.getModifiers()));

        System.out.format("Type Parameters:%n");
        final TypeVariable<?>[] tv = c.getTypeParameters();
        if (tv.length != 0) {
            System.out.format("  ");
            for (final TypeVariable<?> t : tv)
                System.out.format("%s ",t.getName());
            System.out.format("%n%n");
        } else {
            System.out.format("  -- No Type Parameters --%n%n");
        }

        System.out.format("Implemented Interfaces:%n");
        final Type[] intfs = c.getGenericInterfaces();
        if (intfs.length != 0) {
            for (final Type intf : intfs)
                System.out.format("  %s%n",intf.toString());
            System.out.format("%n");
        } else {
            System.out.format("  -- No Implemented Interfaces --%n%n");
        }

        System.out.format("Inheritance Path:%n");
        final List<Class<?>> l = new ArrayList<>();
        printAncestor(c,l);
        if (l.size() != 0) {
            for (final Class<?> cl : l)
                System.out.format("  %s%n",cl.getName());
            System.out.format("%n");
        } else {
            System.out.format("  -- No Super Classes --%n%n");
        }

        System.out.format("Annotations:%n");
        final Annotation[] ann = c.getAnnotations();
        if (ann.length != 0) {
            for (final Annotation a : ann)
                System.out.format("  %s%n",a.toString());
            System.out.format("%n");
        } else {
            System.out.format("  -- No Annotations --%n%n");
        }

    }

    private static void printAncestor(final Class<?> c, final List<Class<?>> l) {
        final Class<?> ancestor = c.getSuperclass();
        if (ancestor != null) {
            l.add(ancestor);
            printAncestor(ancestor,l);
        }
    }

    public static void main(String[] args) {

        try {

            //outputClassDecl(String.class.getName());


            // local class
            class A<B extends Collection<A<B>>> extends ArrayList<A<B>> implements Collection<A<B>> {

            }

            outputClassDecl(A.class);

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}
