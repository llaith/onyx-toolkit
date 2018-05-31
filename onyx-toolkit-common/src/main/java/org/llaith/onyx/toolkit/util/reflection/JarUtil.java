/*
 * Copyright (c) 2016.
 */

package org.llaith.onyx.toolkit.util.reflection;


import org.llaith.onyx.toolkit.util.exception.UncheckedException;

import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class JarUtil {

    /*
     * http://stackoverflow.com/questions/1529611/how-to-write-java-program-
     * which-can-extract-a-jar-file-and-stores-its-data-in-spe
     */
    public static void extractJar(final File jarFile, final String destPath) throws IOException {
        final java.util.jar.JarFile jar = new java.util.jar.JarFile(jarFile);
        final java.util.Enumeration<java.util.jar.JarEntry> en = jar.entries();
        while (en.hasMoreElements()) {
            final java.util.jar.JarEntry file = en.nextElement();
            final java.io.File f = new java.io.File(destPath + java.io.File.separator + file.getName());
            if (file.isDirectory()) {
                f.mkdir();
                continue;
            }
            final java.io.InputStream is = jar.getInputStream(file);
            final java.io.FileOutputStream fos = new java.io.FileOutputStream(f);
            while (is.available() > 0) {
                fos.write(is.read());
            }
            fos.close();
            is.close();
        }
    }


    public static List<String> extractJarNames(final String jarFile) {
        return extractJarNames(new File(jarFile));
    }

    public static List<String> extractJarNames(final File jarFile) {
        try {
            final List<String> entries = new ArrayList<>();

            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> en = jar.entries();
            while (en.hasMoreElements()) {
                final JarEntry file = en.nextElement();
                final String name = file.getName();
                if ((!file.isDirectory()) && (name.endsWith(".class"))) {
                    entries.add(classPathToName(name));
                }
            }
            return entries;
        } catch (IOException e) {
            throw UncheckedException.wrap(e);
        }
    }

    private static String classPathToName(final String path) {
        return path.replace('/','.').substring(0,path.lastIndexOf('.'));
    }

}
