/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;


import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class ClasspathUtil {


    public static void loadClassFromFile(final String name) {
        loadClassFromFile(new File(name));
    }

    public static void loadClassFromFile(final File file) {
        try {
            loadClassFromUrl(file.toURI().toURL());
        } catch (MalformedURLException e) {
            throw UncheckedException.wrap(e);
        }
    }

    public static void loadClassFromUrl(final URL url) {
        try {
            final URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
            final Method method = URLClassLoader.class.getDeclaredMethod("addURL",URL.class);
            method.setAccessible(true);
            method.invoke(sysloader,url);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw UncheckedException.wrap(e);
        }
    }

}

